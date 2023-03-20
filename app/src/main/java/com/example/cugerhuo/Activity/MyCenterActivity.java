package com.example.cugerhuo.Activity;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cugerhuo.R;

/**
 * 指纹验证：
 * @link https://www.bilibili.com/video/BV1664y1d7oG/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author  朱萌
 */
public class MyCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
    }



}