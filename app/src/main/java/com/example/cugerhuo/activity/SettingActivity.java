package com.example.cugerhuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.imessage.MessageActivity;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.views.SlideSwitch;

import java.io.File;

/**
 * 设置
 * @Author: 李柏睿
 * @Time: 2023/3/22 16:40
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 清除缓存按钮
     * @author 施立豪
     * @time 2023/4/18
     */
    private LinearLayout clearCatch;
    private LinearLayout settingLayout;
    /**
     * 用户信息设置按钮
     */
    private LinearLayout settingUserLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();

    }
    /**
     * 初始化界面
     * @author 施立豪
     * @time 2023/4/18
     */
    public void init()
    {
        clearCatch =findViewById(R.id.setting_clear_layout);
        clearCatch.setOnClickListener(this);
        settingLayout = findViewById(R.id.setting_address_layout);
        settingLayout.setOnClickListener(this);
        settingUserLayout=findViewById(R.id.setting_user_layout);
        settingUserLayout.setOnClickListener(this);

    }
    /**
     * 点击事件函数
     * @Author: 李柏睿
     * @Time: 2023/4/22 17:14
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            /**
             * 地址管理
             * @Author: 李柏睿
             * @Time: 2023/4/22 17:16
             */
            case R.id.setting_address_layout:
                startActivity(new Intent(getApplicationContext(), AddressManageActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            /**
             * 清除缓存
             * @Author: 李柏睿
             * @Time: 2023/4/22 17:18
             */
            case R.id.setting_clear_layout:
                setClearCatch(view);
                break;
            /**
             * 用户信息设置
             * @author 唐小莉
             * @time 2023/4/25
             */
            case R.id.setting_user_layout:
                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                break;
        }
    }
    /**
     * 清除缓存功能
     * @author 施立豪
     * @time 2023/4/18
     */
    public void setClearCatch(View view)
    {   //打开文件目录
        File file=new File(getSandboxPath());
        //当前文件是目录，删除目录下所有文件
        System.out.println((getSandboxPath()));
        System.out.println("path"+(getSandboxPath()));
        System.out.println("image"+ UserInfo.getUrl());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                System.out.println(i);
                //如果文件存在则删除
                if (f.exists()) {
                    f.delete();
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        }MyToast.toast(this,"清除缓存成功",3);
    }}
    /**
     * 获取缓存所在目录
     * @author 施立豪
     * @time 2023/4/18
     * @return 路径字符串
     */
    private String getSandboxPath() {
        File externalFilesDir = SettingActivity.this.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }
}