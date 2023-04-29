package com.example.cugerhuo;

import static com.example.cugerhuo.login.loginutils.Constant.THEME_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.mobstat.StatService;
import com.example.cugerhuo.access.SetGlobalIDandUrl;
import com.example.cugerhuo.activity.ErHuoActivity;
import com.example.cugerhuo.graph.GraphOperate;
import com.example.cugerhuo.login.login.OneKeyLoginActivity;
import com.example.cugerhuo.oss.InitOS;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.jaegertracing.Configuration;
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
    Thread tracerThread;
    LottieAnimationView lottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //
//        Intent i1=new Intent(getApplicationContext(), QqLoginActivity.class);
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
         * 链路追踪初始化以及对象存储初始化
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //List<String> a=new ArrayList<String>();
              //  a.add("/storage/emulated/0/Android/data/com.example.cugerhuo/cache/luban_disk_cache/CMP_20230429162710476.jpg");
                List<Integer> a=GraphOperate.productSearch("/storage/emulated/0/Android/data/com.example.cugerhuo/cache/luban_disk_cache/CMP_20230429162710476.jpg");
                for(int i:a)
                {
                    System.out.println(i);
                }
                /**
                 * 对象存储初始化1
                 */
                InitOS m=InitOS.getInstance(getApplicationContext());
                System.out.println("secret"+m.get());
                /**
                 * 图像检索初始化
                 */
                /**
                 * 链路追踪初始化
                 */
                Configuration config = new Configuration("CUG贰货");
                Configuration.SenderConfiguration sender = new Configuration.SenderConfiguration();
                // 将 <endpoint> 替换为控制台概览页面上相应客户端和地域的接入点。
                sender.withEndpoint("http://tracing-analysis-dc-hz.aliyuncs.com/adapt_f6yah647nw@42a790d7a35fc27_f6yah647nw@53df7ad2afe8301/api/traces");
                config.withSampler(new Configuration.SamplerConfiguration().withType("const").withParam(1));
                config.withReporter(new Configuration.ReporterConfiguration().withSender(sender).withMaxQueueSize(10000));
                if(GlobalTracer.isRegistered()){GlobalTracer.register(config.getTracer());}
                return;
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
        //-----------------------/
//        /**
//         * 测试云信用的
//         * @author 朱萌
//         * @time 2023/4/4
//         */
//        Intent i=new Intent(getApplicationContext(), ErHuoActivity.class);
//        startActivity(i);
        //-------------------------/

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
                 * @time 2023/3/19 2023/3/28 更新qq登录
                 */
                /**
                 * 登录信息，存储了手机号，手机号登陆日期；qqopen id(唯一)，qq号登陆日期
                 */
                SharedPreferences loginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                //获得Editor 实例
                SharedPreferences.Editor editor = loginMessage.edit();
                String lastData=""; //手机号登陆日期
                String qqLastData=""; //qq登录日期
                //以key-value形式保存数据

               lastData= loginMessage.getString("LoginData","");

               qqLastData= loginMessage.getString("QqLoginData","");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //当前日期
                Date date=new Date();
                if(!lastData.equals("")||!qqLastData.equals("")){
                    /**
                     * 日期转换,求上次与这次登录的时间差
                     */
                    /**
                     * 计算时间差，单位天
                     */
                    double dayNum=10;   //手机

                    double qqDayNum=10;//qq
                    try {
                        /**
                         * 判断是否为空
                         */
                        if(!lastData.equals("")){
                            dayNum= (date.getTime() - format.parse(lastData).getTime()) / (24 * 60 * 60 * 1000);}
                        if(!qqLastData.equals("")){
                            qqDayNum= (date.getTime() - format.parse(qqLastData).getTime()) / (24 * 60 * 60 * 1000);}
                    } catch ( ParseException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 时间差小于7天，直接进主页，更新日期
                     */
                    if(dayNum<=7||qqDayNum<=7)
                    {
                        System.out.println("newdate"+format.format(date));
                        String time = format.format(date);
                        if(qqDayNum<=dayNum)
                        {
                            /**
                             * 更新登录时间
                             */
                            editor.putString("QqLoginData",time);
                            /**
                             * 初始化全局变量
                             */
                            String qqId=loginMessage.getString("QqId","");
                SetGlobalIDandUrl.setByQq(qqId,StartSplashActivity.this);

//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    /**
//                                     * 获取本地qqId，通过qqId获取ID
//                                     * @author 施立豪
//                                     * @time 2023/4/9
//                                     */
//
//                                    String qqId=LoginMessage.getString("QqId","");
//                                    Tracer tracer = GlobalTracer.get();
//                                    // 创建spann
//                                    int id=-1;
//                                    Span span = tracer.buildSpan("通过qq启动获取用户ID流程").withTag("Oncreate函数：", "子追踪").start();
//                                    try (Scope ignored = tracer.scopeManager().activate(span,true)) {
//                                        // 业务逻辑写这里
//                                        id= UserOperate.GetQqId(qqId,StartSplashActivity.this);
//                                    } catch (Exception e) {
//                                        TracingHelper.onError(e, span);
//                                        throw e;
//                                    } finally {
//                                        span.finish();
//                                    }
//                                    if(id!=-1)
//                                    {
//                                        UserInfo.setID(id);
//                                        String imageUrl="";
//                                        Span span3 = tracer.buildSpan("通过id获取图片url").withTag("Oncreate函数：", "子追踪").start();
//                                        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
//                                            // 调用获取头像url接口
//                                            imageUrl=UserInfoOperate.GetImage(id,StartSplashActivity.this);
//                                        } catch (Exception e) {
//                                            TracingHelper.onError(e, span);
//                                            throw e;
//                                        } finally {
//                                            span.finish();
//                                        }
//                                        /**
//                                         * 得到url
//                                         */
//                                        if(!imageUrl.equals(""))
//                                        {
//                                            /**
//                                             * 获取本地存储图片路径
//                                             */
//                                            SharedPreferences imagePath=getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
//                                            String imageAbsolutePath=imagePath.getString("imagepath","");
//                                            String nativeName= GetFileNameUtil.GetFileName(imageAbsolutePath);
//                                            /**
//                                             * 如果本地和用户表中存储的文件名一致，则直接用本地的
//                                             */
//                                            if(imageUrl.equals(nativeName))
//                                            {
//                                                UserInfo.setUrl(imageAbsolutePath);
//                                            }
//                                            /**
//                                             * 否则下载oss的文件到本地，并修改本地存储的图片路径
//                                             */
//                                            else
//                                            {
//                                                /**
//                                                 * 下载
//                                                 */
//                                                boolean IsDownLoad=false;
//                                                Span span4 = tracer.buildSpan("缓存头像流程").withTag("onCreaate函数：", "子追踪").start();
//                                                try (Scope ignored = tracer.scopeManager().activate(span,true)) {
//                                                    // 调用下载接口
//                                                    IsDownLoad=OssOperate.DownLoad(imageUrl,getSandboxPath()+imageUrl);
//                                                } catch (Exception e) {
//                                                    TracingHelper.onError(e, span);
//                                                    throw e;
//                                                } finally {
//                                                    span.finish();
//                                                }
//                                                if(IsDownLoad)
//                                                    UserInfo.setUrl(getSandboxPath()+imageUrl);
//                                                else
//                                                {
//                                                    Log.e(TAG,"从oss缓存头像URL失败");
//                                                }
//                                                UserInfo.setUrl(getSandboxPath()+imageUrl);
//
//                                            }
//                                        }
//                                        /**
//                                         * 未得到url
//                                         */
//                                        else{
//                                            Log.e(TAG,"Id获取头像URL失败");
//                                        }
//                                    }
//
//                                    else
//                                    {
//                                        System.out.println("qqId获取用户ID失败");
//                                    }
//                                }
//                            }).start();
                        }
                        else
                        {
                            /**
                             * 更新登录时间
                             */
                            editor.putString("LoginData",time);
                            /**
                             * 初始化全局变量
                             * @author 施立豪
                             * @time 2023/4/9
                             */
                            String phoneNumber=loginMessage.getString("PhoneNumber","");
                            SetGlobalIDandUrl.setByPhone(phoneNumber,StartSplashActivity.this);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    /**
//                                     * 获取ID
//                                     */
//                                    String phoneNumber=LoginMessage.getString("PhoneNumber","");
//                                    Tracer tracer = GlobalTracer.get();
//                                    // 创建spann
//                                    int id=-1;
//                                    Span span = tracer.buildSpan("通过phone启动获取用户ID流程").withTag("Oncreate函数：", "子追踪").start();
//                                    try (Scope ignored = tracer.scopeManager().activate(span,true)) {
//                                        // 业务逻辑写这里
//                                        id= UserOperate.GetId(phoneNumber,StartSplashActivity.this);
//                                    } catch (Exception e) {
//                                        TracingHelper.onError(e, span);
//                                        throw e;
//                                    } finally {
//                                        span.finish();
//                                    }
//                                    /**
//                                     * 得到ID，用id获取图片url
//                                     */
//                                    if(id!=-1)
//                                    {
//                                        UserInfo.setID(id);
//                                        String imageUrl="";
//                                        Span span3 = tracer.buildSpan("通过id获取图片url").withTag("Oncreate函数：", "子追踪").start();
//                                        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
//                                            // 调用获取头像url接口
//                                            imageUrl=UserInfoOperate.GetImage(id,StartSplashActivity.this);
//                                        } catch (Exception e) {
//                                            TracingHelper.onError(e, span);
//                                            throw e;
//                                        } finally {
//                                            span.finish();
//                                        }
//                                        /**
//                                         * 得到url
//                                         */
//                                        if(!imageUrl.equals(""))
//                                        {
//                                            /**
//                                             * 获取本地存储图片路径
//                                             */
//                                           SharedPreferences imagePath=getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
//                                           String imageAbsolutePath=imagePath.getString("imagepath","");
//                                           String nativeName= GetFileNameUtil.GetFileName(imageAbsolutePath);
//                                            /**
//                                             * 如果本地和用户表中存储的文件名一致，则直接用本地的
//                                             */
//                                           if(imageUrl.equals(nativeName))
//                                           {
//                                               UserInfo.setUrl(imageAbsolutePath);
//                                           }
//                                           /**
//                                            * 否则下载oss的文件到本地，并修改本地存储的图片路径
//                                            */
//                                           else
//                                           {
//                                               /**
//                                                * 下载
//                                                */
//                                               boolean IsDownLoad=false;
//                                               Span span4 = tracer.buildSpan("缓存头像流程").withTag("onCreaate函数：", "子追踪").start();
//                                               try (Scope ignored = tracer.scopeManager().activate(span,true)) {
//                                                   // 调用下载接口
//                                                   IsDownLoad=OssOperate.DownLoad(imageUrl,getSandboxPath()+imageUrl);
//                                               } catch (Exception e) {
//                                                   TracingHelper.onError(e, span);
//                                                   throw e;
//                                               } finally {
//                                                   span.finish();
//                                               }
//                                               if(IsDownLoad)
//                                               UserInfo.setUrl(getSandboxPath()+imageUrl);
//                                               else
//                                               {
//                                                   Log.e(TAG,"从oss缓存头像URL失败");
//                                               }
//                                               UserInfo.setUrl(getSandboxPath()+imageUrl);
//
//                                           }
//                                        }
//                                        /**
//                                         * 未得到url
//                                         */
//                                        else{
//                                            Log.e(TAG,"Id获取头像URL失败");
//                                        }
//                                    }
//                                    else
//                                    {
//                                        Log.e(TAG,"phone获取用户ID失败");
//
//                                    }
//                                }
//                            }).start();
                        }
                        //异步写入
                        editor.apply();
                        /**
                         * 登录信息没过期直接进入主页
                         */

                        Intent i=new Intent(getApplicationContext(), ErHuoActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        finish();
                    return;
                    }}
                /**
                 * 本地存储没用或者过期进入登陆页面
                 */
                Intent i=new Intent(getApplicationContext(), OneKeyLoginActivity.class);
                i.putExtra(THEME_KEY, 1);
                startActivity(i);
                overridePendingTransition(0,0);
                /**
                 * 跳转到OneKeyLoginActivity界面，并结束当前界面生命周期
                 * 当用户在下一个节目点击返回 则直接退出app 而不是返回当前页面
                 */
                finish();
            }
        },4000);
    }
    /**
     * 创建自定义输出目录
     *
     * @return 路径
     */
    private String getSandboxPath() {
        File externalFilesDir = this.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }


}
