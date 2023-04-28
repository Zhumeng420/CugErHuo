package com.example.cugerhuo.activity;

import static com.example.cugerhuo.activity.MyCenterActivity.focusNum;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewRecommenduser;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.views.ConcernDialog;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 粉丝列表
 * @author carollkarry
 * @time 2023/4/25
 */
public class FansActivity extends AppCompatActivity {


    /**
     * recyclerViewFans 粉丝列表
     * adapter recycler适配
     * fansUserInfo 存储粉丝列表信息
     * positionClick点击的item position
     * @author 唐小莉
     * @time 2023/4/28
     */

    private RecyclerView recyclerViewFans;
    private RecyclerViewAdapter adapter;
    private List<PartUserInfo> fansUserInfo =new ArrayList<>();
    private final MyHandler MyHandler =new MyHandler();
    private int positionClick;

    /**
     * 用户id
     */
    private  int id1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        recyclerViewFans=findViewById(R.id.recycler_Fans);
        recyclerViewFans.setLayoutManager(new LinearLayoutManager(this));
        id1= UserInfo.getid();
        // 5、开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            //获取关注列表id
            List<Integer> getFansIds=new ArrayList<>();
            //获取关注用户信息
            /**
             * 获取关注列表id
             */
            getFansIds= UserOperate.getFansId(id1,FansActivity.this);
            /**
             * 建立连接对象
             */
            LettuceBaseCase lettuce=new LettuceBaseCase();
            /**
             * 获取连接
             */
            RedisCommands<String, String> con=lettuce.getSyncConnection();
            /**
             * 通过连接调用查询
             */
            for(int i=0;i<getFansIds.size();i++){
                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,getFansIds.get(i),FansActivity.this);

                System.out.println("粉丝idididdididi"+part.getUserName());
                System.out.println("简介简介简介简介"+part.getSignature());
                fansUserInfo.add(part);
                /**
                 * 如果互关，则将值设为5
                 */
                if(UserOperate.isAllFocus(id1,getFansIds.get(i),FansActivity.this)){
                    fansUserInfo.get(i).setConcern(5);
                }
                /**
                 * 没有互关，设为1
                 */
                else{
                    fansUserInfo.get(i).setConcern(4);
                }
                System.out.println("粉丝粉丝粉丝粉丝粉丝-------"+part.getImageUrl());
            }

//            msg.arg2=fansNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
            /**
             * 关闭连接啊
             */
            lettuce.close();
        }).start();
    }

    /**
     * 消息发送接收，异步更新UI
     * @author 唐小莉
     * @time 2023/4/25
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 获取关注列表
                 */
                case 1:
                    adapter = new RecyclerViewAdapter(getActivity(),fansUserInfo);
                    recyclerViewFans.setAdapter(adapter);
                    /**
                     * 点击关注item进行跳转并传值过去
                     */
                    adapter.setOnItemUserClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            positionClick=position;
                            Intent intent=new Intent(getActivity(), OtherPeopleActivity.class);
                            intent.putExtra("concernUser",fansUserInfo.get(position));
                           // intent.putExtra("focusNum",focusNum);
                            //startActivity(intent);
                            startActivityForResult(intent,2);
                        }
                    });
                    adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            positionClick=position;
                            /**
                             * 如果是互关，则进行弹窗
                             */
                            if(fansUserInfo.get(position).getConcern()==5){
                                /**
                                 * 点击已关注实现弹窗
                                 */
                                ConcernDialog concernDialog=new ConcernDialog(getActivity());
                                /**
                                 * 确定按钮回调
                                 */
                                concernDialog.setConfirmListener(new ConcernDialog.ConfirmListener() {
                                    @Override
                                    public void onConfirmClick() {
                                        /**
                                         * 点击确定按钮后判断是否取消成功
                                         */
                                             new Thread(new FansActivity.MyRunnableDeleteConcernOperate()).start();
                                            adapter.notifyItemChanged(position,"4");
                                            focusNum--;
                                    }
                                });
                                concernDialog.show();
                            }
                            /**
                             * 如果是回粉状态，点击进行关注
                             */
                            else{
                                new Thread(new FansActivity.MyRunnableConcernOperate()).start();
                                adapter.notifyItemChanged(position,"5");
                                focusNum++;
                            }

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取上个页面返回的数据并进行页面响应
     * @param requestCode
     * @param resultCode
     * @param data
     * @author 唐小莉
     * @time 2023/4/28
     */
    @Override
    public  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            int data1 = data.getIntExtra("isConcern",0);
            fansUserInfo.get(positionClick).setConcern(data1);
            adapter.setPartUserInfoPosition(positionClick,data1);
            /**
             * 在他人界面点击了取消关注，回到回粉状态
             */
            if(data1==4){
                adapter.notifyItemChanged(positionClick,"4");
            }
            /**
             * 在他人界面点击关注，且关注后为互相关注状态
             */
            else if(data1==5){
                adapter.notifyItemChanged(positionClick,"5");
            }
            System.out.println("hello concernActivity"+data1);
        }
    }

    /**
     * 开启线程,取消关注
     * @author 唐小莉
     * @time 2023/4/27
     */
    class MyRunnableDeleteConcernOperate implements  Runnable{
        @Override
        public void run() {
            UserOperate.getIfDeleteConcern(id1,fansUserInfo.get(positionClick).getId(),getActivity());
        }
    }

    /**
     * 开启线程,关注
     * @author 唐小莉
     * @time 2023/4/27
     */
    class MyRunnableConcernOperate implements Runnable{
        @Override
        public void run() {
            boolean su=UserOperate.setConcern(id1,fansUserInfo.get(positionClick).getId(),getActivity());
            System.out.println("concernhhhhh success"+su);
        }
    }

}