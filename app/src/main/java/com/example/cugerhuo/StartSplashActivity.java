package com.example.cugerhuo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

/**
 * APP启动动画类（已经设置为了APP启动类）：
 * 素材：https://lottiefiles.com/133076-welcome?lang=zh_CN
 * 教程：https://www.bilibili.com/video/BV1Gy4y1M7Rw/?spm_id_from=333.337.search-card.all.click&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 */
public class StartSplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splash);
    }
}