package com.example.cugerhuo.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;

/**
 * 登录模块：采用当前手机号码一键登录、第三方登录
 * 第三方登录：
 * @link https://www.justauth.cn/
 * @author 朱萌
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}