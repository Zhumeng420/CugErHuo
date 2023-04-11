package com.example.cugerhuo.login.loginutils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.cugerhuo.R;

/**
 * 登录失败后进入该activity，界面为短信登录（具体并未实现）
 * @author 施立豪
 */
public class MessageActivity extends Activity {
    private Button mAuthButton;
    private EditText mNumberEt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


    }
}
