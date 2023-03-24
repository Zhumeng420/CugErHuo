package com.example.cugerhuo;

import static com.example.cugerhuo.FastLogin.loginUtils.Constant.THEME_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.mobstat.StatService;
import com.example.cugerhuo.Activity.ErHuoActivity;
import com.example.cugerhuo.FastLogin.login.OneKeyLoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.opentracing.util.GlobalTracer;

/**
 * APP启动动画类（已经设置为了APP启动类）：
 * 素材：
 * @link https://lottiefiles.com/133076-welcome?lang=zh_CN
 * 教程：
 * @link https://www.bilibili.com/video/BV1Gy4y1M7Rw/?spm_id_from=333.337.search-card.all.click&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author 朱萌
 */

/**
 * 动态权限获取组件:XXPermissions
 * @link https://github.com/getActivity/XXPermissions
 */

/**
 * 动态TextView组件;
 * https://github.com/hanks-zyh/HTextView
 */
public class StartSplashActivity extends AppCompatActivity {
    /**
     * 动画组件，用于在界面中显示动画
     * img变量为启动页背景图片
     * logo变量为该app的logo图片
     * lottie变量为lottie动画
     * @author 唐小莉
     * @time 2023/3/4 14:12
     * @link https://www.bilibili.com/video/BV14o4y197t5/?spm_id_from=333.999.0.0&vd_source=60999ec892c4a648641fb136253c49c5
     */
    ImageView img;
    ImageView logo;
    LottieAnimationView lottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
//        Intent i1=new Intent(getApplicationContext(), ErHuoActivity.class);
//        startActivity(i1);
//        finish();
        /**
         * 检测系统启动代码
         * @author 朱萌
         * @time 2023/3/2 19：12
         */
        StatService.setDebugOn(true);
        StatService.autoTrace(this, true, false);
        /**
         * 链路追踪初始化
         */
        // 将manualDemo替换为您的应用名称。
        new Thread(new Runnable() {
            @Override
            public void run() {
                io.jaegertracing.Configuration config = new io.jaegertracing.Configuration("CUG贰货");
                io.jaegertracing.Configuration.SenderConfiguration sender = new io.jaegertracing.Configuration.SenderConfiguration();
                // 将 <endpoint> 替换为控制台概览页面上相应客户端和地域的接入点。
                sender.withEndpoint("http://tracing-analysis-dc-hz.aliyuncs.com/adapt_f6yah647nw@42a790d7a35fc27_f6yah647nw@53df7ad2afe8301/api/traces");
                config.withSampler(new io.jaegertracing.Configuration.SamplerConfiguration().withType("const").withParam(1));
                config.withReporter(new io.jaegertracing.Configuration.ReporterConfiguration().withSender(sender).withMaxQueueSize(10000));
                GlobalTracer.register(config.getTracer());
            }
        }).start();


        /**
         * 启动动画
         * @author 唐小莉
         * @time 2023/3/2 21:47
         */
        setContentView(R.layout.activity_start_splash);
        /**
         * 依次根据id找到对应控件
         * @author 唐小莉
         * @time 2023/3/4 14:12
         */
        img=findViewById(R.id.img);
        logo=findViewById(R.id.logo);
        lottie=findViewById(R.id.lottie);

        /**
         * 设置lottie动画的动画效果
        translationX() 动画X轴偏移量,其中img设置上滑效果，故设置translationY(-2200)
         logo与lottie设置为下滑，则将translationY设置为1600
        setDuration() 设置动画运行时间
        setStartDelay() 设置动画延迟时间，此时为2800ms,等lottie动画完成后进行
         @author 唐小莉
         @time 2023/3/2 21:47
         */
        img.animate().translationY(-2200).setDuration(1000).setStartDelay(2800);
        logo.animate().translationY(1600).setDuration(1000).setStartDelay(2800);
        lottie.animate().translationY(1600).setDuration(1000).setStartDelay(2800);

        /**
         * 由启动页面跳转至主页，同时等待时间设为4000ms，刚好将启动动画演示完以及页面滑动完成
         * @author 唐小莉
         * @time 2023/3/3 10:35
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * 查看本地存储是否可用
                 * @author 施立豪
                 * @time 2023/3/19
                 */
                SharedPreferences LoginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                //获得Editor 实例
                SharedPreferences.Editor editor = LoginMessage.edit();
                String LastData="";
                //以key-value形式保存数据
               LastData= LoginMessage.getString("LoginData","");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                if(LastData!=""){
                    /**
                     * 日期转换,求上次与这次登录的时间差
                     */

                    /**
                     * 计算时间差，单位天
                     */
                    double DayNum=10;
                    try {

                        DayNum= (date.getTime() - format.parse(LastData).getTime()) / (24 * 60 * 60 * 1000);

                    } catch ( ParseException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 时间差小于7天，直接进主页，更新日期
                     */
                    if(DayNum<=7)
                    {

                        System.out.println("newdate"+format.format(date));
                        String Time = format.format(date);
                        editor.putString("LoginData",Time);
                        editor.apply();
                        /**
                         * 登录信息没过期直接进入主页
                         */
                        Intent i=new Intent(getApplicationContext(), ErHuoActivity.class);
                        startActivity(i);
                        finish();
                    return;
                    }}
                /**
                 * 本地存储没用或者过期进入登陆页面
                 */
                Intent i=new Intent(getApplicationContext(), OneKeyLoginActivity.class);
                i.putExtra(THEME_KEY, 1);
                startActivity(i);
                /**
                 * 跳转到OneKeyLoginActivity界面，并结束当前界面生命周期
                 * 当用户在下一个节目点击返回 则直接退出app 而不是返回当前页面
                 */
                finish();
            }
        },4000);

    }

}