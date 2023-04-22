package com.example.cugerhuo.activity;

import static android.graphics.Color.parseColor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.views.SlideSwitch;

import java.io.File;

/**
 * 地址新建/编辑
 * @Author: 李柏睿
 * @Time: 2023/4/22 13:23
 */
public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener{
    private SlideSwitch sSwitch;
    private EditText addressName,addressPhone,addressZone,addressDetail;
    private TextView deleteAddress;
    ImageView returnImg;
    Button commitAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initView();
        sSwitch = (SlideSwitch) this.findViewById(R.id.slide_switch);
        sSwitch.setOnStateChangedListener(new SlideSwitch.OnStateChangedListener(){
            @Override
            public void onStateChanged(boolean state) {
                if(true == state)
                {
                    Toast.makeText(EditAddressActivity.this, "开关已打开", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EditAddressActivity.this, "开关已关闭", Toast.LENGTH_SHORT).show();
                }
            }

        });
        initIntendInfo();
    }

    /**
     * 各控件初始化
     * @Author: 李柏睿
     * @Time: 2023/4/22 19:31
     */
    public void initView(){
        addressName = findViewById(R.id.address_user);
        addressPhone = findViewById(R.id.address_user_number_phone);
        addressZone = findViewById(R.id.address_user_zone);
        addressDetail = findViewById(R.id.address_detail);
        deleteAddress = findViewById(R.id.address_delete);
        returnImg = findViewById(R.id.return_img);
        commitAddress = findViewById(R.id.commit_address);
        returnImg.setOnClickListener(this);
        commitAddress.setOnClickListener(this);
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
             * 提交保存信息
             * @Author: 李柏睿
             * @Time: 2023/4/22 20:13
             */
            case R.id.commit_address:

                break;
            default:
                break;
        }
    }

    /**
     * 获取上个页面传递过来的信息
     * @Author: 李柏睿
     * @Time: 2023/4/22 19:30
     */
    public void initIntendInfo(){
        /**
         * 直接获取上个界面传递的用户信息
         */
        Intent intent =getIntent();
        int flag = 0 ;
        flag= (int) intent.getSerializableExtra("flag");
        /**
         * 将信息进行加载显示
         */
        if(flag==1){
            addressName.setText("朱古力");
            addressPhone.setText("13765627632");
            addressZone.setText("湖北省武汉市");
            addressDetail.setText("中国地质大学未来城校区");
            sSwitch.setChecked();
            deleteAddress.setVisibility(View.VISIBLE);
        }


    }
}