package com.example.cugerhuo.activity;


import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.AddressInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.access.user.XuanShangInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewAddressAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewXuanShangAdapter;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.views.ConcernDialog;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 地址管理
 * @Author: 李柏睿
 * @Time: 2023/4/20 16:22
 */
public class AddressManageActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    /**地址信息列表*/
    private List<AddressInfo> addressInfos =new ArrayList<>();
    /**positionClick  记录目前点击的item位置*/
    private int positionClick;
    /**RecyclerView适配器*/
    private RecyclerViewAddressAdapter adapter;
    private final AddressManageActivity.MyHandler MyHandler =new AddressManageActivity.MyHandler();
    ImageView returnImg;
    Button addAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        initView();
        // 开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            MyHandler.sendMessage(msg);
        }).start();
    }

    /**
     * 初始化各个控件，找到对应的组件
     * @Author: 李柏睿
     * @Time: 2023/4/22 16:19
     */
    public void initView(){
        recyclerView = findViewById(R.id.recyclerView_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for(int i=0;i<6;i++){
            AddressInfo part= new AddressInfo();
            addressInfos.add(part);
        }
        RecyclerViewAddressAdapter adapter = new RecyclerViewAddressAdapter(getActivity(), addressInfos);
        recyclerView.setAdapter(adapter);
        returnImg = findViewById(R.id.return_img);
        addAddress = findViewById(R.id.add_address);
        returnImg.setOnClickListener(this);
        addAddress.setOnClickListener(this);
    }

    /**
     * 点击事件函数
     * @Author: 李柏睿
     * @Time: 2023/4/22 20:12
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            /**
             * 返回上一级
             * @Author: 李柏睿
             * @Time: 2023/4/22 20:13
             */
            case R.id.return_img:
                finish();
                break;
            /**
             * 添加地址信息
             * @Author: 李柏睿
             * @Time: 2023/4/22 20:13
             */
            case R.id.add_address:

                int flag = 0;
                Intent intent=new Intent(AddressManageActivity.this, EditAddressActivity.class);
                intent.putExtra("flag",flag);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            default:
                break;
        }
    }

    /**
     * 消息发送接收，异步更新UI
     * @Author: 李柏睿
     * @Time: 2023/4/22 19:48
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 获取地址信息列表
                 */
                case 1:
                    adapter = new RecyclerViewAddressAdapter(AddressManageActivity.this,addressInfos);
                    recyclerView.setAdapter(adapter);
                    /**
                     * 点击item进行跳转并传值过去
                     */
                    adapter.setOnItemClickListener(new RecyclerViewAddressAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int flag = 1;
                            Intent intent=new Intent(AddressManageActivity.this, EditAddressActivity.class);
                            intent.putExtra("flag",flag);
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
}