package com.example.cugerhuo.activity;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.adapter.Concern;
import com.example.cugerhuo.activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.LettuceBaseCase;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 关注列表
 * @author 唐小莉
 * @Time 2023/4/4 21:02
 */
public class ConcernActivity extends AppCompatActivity {

    private  RecyclerView recyclerView2;
    private List<PartUserInfo> getFocusInfo=new ArrayList<>();
    private final ConcernActivity.mHandler mHandler=new mHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);

        //concern是自定义的存储关注列表的用户信息的类

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));


        // 5、开启线程
        new Thread(() -> {

            Message msg = Message.obtain();
            msg.arg1 = 1;
            //获取关注列表id
            List<Integer> getFocusIds=new ArrayList<>();
            //获取关注用户信息

            /**
             * 查询本地存储
             * @author 施立豪
             * @time 2023/3/27
             */
            SharedPreferences loginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
            //获得Editor 实例
            SharedPreferences.Editor editor = loginMessage.edit();
            String id=loginMessage.getString("Id","");
            int id1;
            /**
             * 如果当前本地没有存储id，先查询id并持久化
             */
            if("".equals(id))
            {

                id1= UserOperate.getId(loginMessage.getString("PhoneNumber",""),ConcernActivity.this);
                editor.putString("Id", String.valueOf(id1));
                editor.apply();

            }
            /**
             * 本地有id，则查询id
             */
            else{
                id1=Integer.parseInt(id);

            }
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
                    part.setUserId(getFocusIds.get(i));
                    getFocusInfo.add(part);
                    System.out.println("关注关注关注关注-------"+part.getImageUrl());
                }
            /**
             * 关闭连接
             */
            lettuce.close();

//            msg.arg2=fansNum;
            //4、发送消息
            mHandler.sendMessage(msg);
        }).start();
    }

    /**
     * 消息发送接收，异步更新UI
     * @author 唐小莉
     * @time 2023/4/11
     */
    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 获取关注列表
                 */
                case 1:
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),getFocusInfo);
                    recyclerView2.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    }


}