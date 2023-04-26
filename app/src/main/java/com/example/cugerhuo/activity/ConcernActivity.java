package com.example.cugerhuo.activity;

import static com.example.cugerhuo.activity.MyCenterActivity.focusNum;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * 关注列表
 * @author 唐小莉
 * @Time 2023/4/4 21:02
 */
public class ConcernActivity extends AppCompatActivity {

    /**
     * recyclerView2 关注列表
     */
    private  RecyclerView recyclerView2;

    private RecyclerView revUser;
    /**
     * getFocusInfo 关注列表用户信息
     */
    private List<PartUserInfo> getFocusInfo=new ArrayList<>();
    private final MyHandler MyHandler =new MyHandler();
    /**
     * positionClick  记录目前点击的item位置
     */
    private int positionClick;
    /**
     * 判断是否取消关注成功
     */
    private boolean isCancelSuccess=false;
    /**
     * 用户id
     */
    private  int id1;
    /**
     * 推荐用户id列表
     */
    private List<Integer> recommendId=new ArrayList<>();
    /**
     * 用于存储推荐关注用户的所有信息
     */
    private List<PartUserInfo> rePartUserInfo=new ArrayList<>();

    private  RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);
        /**
         * 初始化控件
         */
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        revUser=findViewById(R.id.rv_user);
        revUser.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));

        id1= UserInfo.getid();

        // 5、开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            //获取关注列表id
            List<Integer> getFocusIds=new ArrayList<>();
            //获取关注用户信息
            /**
             * 获取关注列表id
             */
            getFocusIds=UserOperate.getConcernId(id1,ConcernActivity.this);
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
            for(int i=0;i<getFocusIds.size();i++){
                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,getFocusIds.get(i),ConcernActivity.this);
                getFocusInfo.add(part);
                /**
                 * 如果互关，则将值设为2
                 */
                if(UserOperate.isAllFocus(id1,getFocusIds.get(i),ConcernActivity.this)){
                    getFocusInfo.get(i).setConcern(2);
                }
                /**
                 * 没有互关，设为1
                 */
                else{
                    getFocusInfo.get(i).setConcern(1);
                }
            }
//            msg.arg2=fansNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
            /**
             * 关闭连接啊
             */
            lettuce.close();
        }).start();

        /**
         * 获取推荐关注用户信息
         */
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 2;
            UserOperate userOperate=new UserOperate();
            recommendId=userOperate.getRecommend(id1,ConcernActivity.this);
//            /**
//             * 建立连接对象
//             */
//            LettuceBaseCase lettuce=new LettuceBaseCase();
//            /**
//             * 获取连接
//             */
//            RedisCommands<String, String> con=lettuce.getSyncConnection();
            for(int i=0;i<recommendId.size();i++){
                System.out.println("idididdidiiiidddd"+recommendId.get(i));
               // PartUserInfo partUserInfo=UserInfoOperate.getInfoFromMysql(recommendId.get(i),ConcernActivity.this);
               // System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+partUserInfo.getId());
               // rePartUserInfo.add(partUserInfo);
            }
            MyHandler.sendMessage(msg);
            /**
             * 关闭连接啊
             */
            //lettuce.close();
        }).start();
    }
    /**
     * 消息发送接收，异步更新UI
     * @author 唐小莉
     * @time 2023/4/11
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
                    adapter = new RecyclerViewAdapter(getActivity(),getFocusInfo);
                    recyclerView2.setAdapter(adapter);
                    adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            positionClick=position;
                            System.out.println("positiohhhhhhh"+positionClick);
                            if(getFocusInfo.get(position).getConcern()==1||getFocusInfo.get(position).getConcern()==2){
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
                                            System.out.println("issuccess"+isCancelSuccess);
                                            new Thread(new ConcernActivity.MyRunnableDeleteConcernOperate()).start();
                                            if(getFocusInfo.get(position).getConcern()==1){
                                                adapter.notifyItemChanged(position,"1");
                                            }
                                            else {
                                                adapter.notifyItemChanged(position,"3");
                                            }
                                            focusNum--;
                                    }
                                });
                                concernDialog.show();
                            }
                            else{
                                new Thread(new ConcernActivity.MyRunnableConcernOperate()).start();

                                /**
                                 * 如果此时属于未关注状态，且之前不是互相关注
                                 */
                                if(getFocusInfo.get(position).getConcern()==0){
                                    adapter.notifyItemChanged(position,"0");
                                }
                                /**
                                 * 互相关注
                                 */
                                else {
                                    adapter.notifyItemChanged(position,"2");
                                }
                                focusNum++;
                            }
                        }
                    });
                    /**
                     * 点击关注item进行跳转并传值过去
                     */
                    adapter.setOnItemUserClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            positionClick=position;
                            Intent intent=new Intent(getActivity(), OtherPeopleActivity.class);
                            intent.putExtra("concernUser",getFocusInfo.get(position));
                            intent.putExtra("focusNum",focusNum);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                        }
                    });
                    break;
                /**
                 * 推荐列表
                 * @author 唐小莉
                 * @time 2023/4/26
                 */
                case 2:
                    RecyclerViewRecommenduser adapter2=new RecyclerViewRecommenduser(getActivity());
                    revUser.setAdapter(adapter2);
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
     */
    @Override
    public  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            int data1 = data.getIntExtra("isConcern",0);
            getFocusInfo.get(positionClick).setConcern(data1);
            adapter.setPartUserInfoPosition(positionClick,data1);
            /**
             * 在他人界面点击了取消关注，且取消前不是互相关注状态
             */
            if(data1==0){
                adapter.notifyItemChanged(positionClick,1);
            }
            /**
             * 在他人界面点击关注，且关注后为互相关注状态
             */
            else if(data1==2){
                adapter.notifyItemChanged(positionClick,"2");
            }
            /**
             * 在他人界面点击取消关注，且取消前是互相关注状态
             */
            else if(data1==3){
                adapter.notifyItemChanged(positionClick,"3");
            }
            /**
             * 在他人界面点击关注，且关注后不是互相关注状态
             */
            else {
                adapter.notifyItemChanged(positionClick,0);
            }
            System.out.println("hello concernActivity"+data1);
        }
    }
    /**
     * 开启线程,取消关注
     * @author 唐小莉
     * @time 2023/4/13
     */
    class MyRunnableDeleteConcernOperate implements  Runnable{
        @Override
        public void run() {

            isCancelSuccess=UserOperate.getIfDeleteConcern(id1,getFocusInfo.get(positionClick).getId(),getActivity());
            System.out.println("issuccess"+isCancelSuccess);
        }
    }
    /**
     * 开启线程,关注
     * @author 唐小莉
     * @time 2023/4/13
     */
    class MyRunnableConcernOperate implements Runnable{
        @Override
        public void run() {
            boolean su=UserOperate.setConcern(id1,getFocusInfo.get(positionClick).getId(),getActivity());
            System.out.println("concernhhhhh success"+su);
        }
    }
}