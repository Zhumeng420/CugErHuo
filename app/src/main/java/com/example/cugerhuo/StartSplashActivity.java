package com.example.cugerhuo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.mobstat.StatService;
import com.example.cugerhuo.Activity.ErHuoActivity;

/**
 * APP启动动画类（已经设置为了APP启动类）：
 * 素材：
 * @link https://lottiefiles.com/133076-welcome?lang=zh_CN
 * 教程：
 * @link https://www.bilibili.com/video/BV1Gy4y1M7Rw/?spm_id_from=333.337.search-card.all.click&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author 朱萌
 */
public class StartSplashActivity extends AppCompatActivity {
    /**
     * 动画组件，用于在界面中显示动画
     */
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 检测系统启动代码
         * @author 朱萌
         * @time 2023/3/2 19：12
         */
        StatService.setDebugOn(true);// 开启自动埋点统计
        StatService.autoTrace(this, true, false);

        /**
         * 启动动画
         * @author 唐小莉
         * @time 2023/3/2 21:47
         */
        setContentView(R.layout.activity_start_splash);
        lottie=findViewById(R.id.lottie);

        /**
         * 设置lottie动画的动画效果
        translationX() 动画X轴偏移量
        setDuration() 设置动画运行时间
        setStartDelay() 设置动画延迟时间，此时为0，则不进行延迟
         @author 唐小莉
         @time 2023/3/2 21:47
         */
        lottie.animate().translationX(0).setDuration(2000).setStartDelay(0);//


        /**
         * 由启动页面跳转至主页，同时等待时间设为9000ms，刚好将启动动画演示完
         * @author 唐小莉
         * @time 2023/3/3 10:35
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), ErHuoActivity.class);
                startActivity(i);
                /**
                 * 跳转到ErHuoActivity界面，并结束当前界面生命周期
                 * 当用户在下一个节目点击返回 则直接退出app 而不是返回当前页面
                 */
                finish();
            }
        },9000);

    }

}