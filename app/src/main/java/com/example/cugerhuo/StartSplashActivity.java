package com.example.cugerhuo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cugerhuo.Activity.ErHuoActivity;

/**
 * APP启动动画类（已经设置为了APP启动类）：
 * 素材：https://lottiefiles.com/133076-welcome?lang=zh_CN
 * 教程：https://www.bilibili.com/video/BV1Gy4y1M7Rw/?spm_id_from=333.337.search-card.all.click&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 */
public class StartSplashActivity extends AppCompatActivity {
    TextView appname;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splash);
        //appname=findViewById(R.id.appname);
        lottie=findViewById(R.id.lottie);

        //appname.animate().translationY(-1400).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), ErHuoActivity.class);
                startActivity(i);
            }
        },5000);
    }
}