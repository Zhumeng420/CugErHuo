package com.example.cugerhuo.activity;

import static com.example.cugerhuo.activity.MyCenterActivity.focusNum;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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


        // 5、开启线程
        new Thread(() -> {

            Message msg = Message.obtain();
            msg.arg1 = 1;
            //获取关注列表id
            List<Integer> getFocusIds=new ArrayList<>();
            //获取关注用户信息

           id1= UserInfo.getid();
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
                System.out.println("关注idididdididi"+part.getUserName());
                System.out.println("简介简介简介简介"+part.getSignature());
                getFocusInfo.add(part);
                getFocusInfo.get(i).setConcern(true);
                System.out.println("关注关注关注关注-------"+part.getImageUrl());
            }
            /**
             * 关闭连接啊
             */
            lettuce.close();

//            msg.arg2=fansNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
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
                                   if(getFocusInfo.get(position).getConcern()){
                                       System.out.println("issuccess"+isCancelSuccess);
                                       new Thread(new ConcernActivity.MyRunnableDeleteConcernOperate()).start();
                                       adapter.notifyItemChanged(position,"true");
                                       focusNum--;
                                   }
                                   else{
                                       new Thread(new ConcernActivity.MyRunnableConcernOperate()).start();
                                       adapter.notifyItemChanged(position,"false");
                                       focusNum++;
                                   }
                                }
                            });
                            concernDialog.show();
                        }
                    });
                    /**
                     * 点击关注item进行跳转并传值过去
                     */
                    adapter.setOnItemUserClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(getActivity(), OtherPeopleActivity.class);
                            intent.putExtra("concernUser",getFocusInfo.get(position));
                            intent.putExtra("focusNum",focusNum);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
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
     */
    @Override
    public  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            boolean data1 = data.getBooleanExtra("isConcern",true);
            getFocusInfo.get(positionClick).setConcern(data1);
            adapter.setPartUserInfoPosition(positionClick,data1);
            if(data1){
                adapter.notifyItemChanged(positionClick,false);
            }
            else{
                adapter.notifyItemChanged(positionClick,true);
            }
            System.out.println("hello concernActivity"+data1);
        }
    }
    /**
     * 开启线程
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

    class MyRunnableConcernOperate implements Runnable{
        @Override
        public void run() {
            boolean su=UserOperate.setConcern(id1,getFocusInfo.get(positionClick).getId(),getActivity());
            System.out.println("concernhhhhh success"+su);
        }
    }


}