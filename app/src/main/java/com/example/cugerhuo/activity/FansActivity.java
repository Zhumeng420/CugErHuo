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
     */

    private RecyclerView recyclerViewFans;
    private RecyclerViewAdapter adapter;
    private List<PartUserInfo> fansUserInfo =new ArrayList<>();
    private final MyHandler MyHandler =new MyHandler();

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
                    adapter = new RecyclerViewAdapter(getActivity(),fansUserInfo);
                    recyclerViewFans.setAdapter(adapter);
                    /**
                     * 点击关注item进行跳转并传值过去
                     */
                    adapter.setOnItemUserClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(getActivity(), OtherPeopleActivity.class);
                            intent.putExtra("concernUser",fansUserInfo.get(position));
                           // intent.putExtra("focusNum",focusNum);
                            //startActivity(intent);
                            startActivityForResult(intent,2);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

}