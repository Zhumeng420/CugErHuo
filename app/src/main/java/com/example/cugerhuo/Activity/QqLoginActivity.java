package com.example.cugerhuo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.DataAccess.User.UserOperate;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.NameUtil;
import com.example.cugerhuo.tools.TracingHelper;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * QQ登录处理，qq openid本地化存储
 * @author 施立豪
 * @time 2023/3/27
 */
public class QqLoginActivity extends AppCompatActivity {
    /**
     * 腾讯接口实例
     */
    Tencent mTencent;
    /**
     * 登录按钮
     */
    Button LoginButton;
    /**
     *  监听接口结果
     */
    BaseUiListener listener;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listener=new BaseUiListener();
        setContentView(R.layout.qq_login);
        LoginButton=findViewById(R.id.lbu);
    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    // 其中APP_ID是分配给第三方应用的appid，类型为String。
    // 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值
        mTencent = Tencent.createInstance("102046332", this.getApplicationContext(),"com.example.cugerhuo.fileprovider");
        /**
         * 获取授权
         */
        Tencent.setIsPermissionGranted(true);
        /**
         * 设置监听器
         */
        LoginButton.setOnClickListener(this::login);
    }
    /**
     * 调用登录接口
     * @param view 当前页面
     * @time 2023/3/27
     */
    public void login(View view)
    {
        mTencent = Tencent.createInstance("102046332", this.getApplicationContext(),"com.example.cugerhuo.fileprovider");
        Tencent.setIsPermissionGranted(true);

        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "get_simple_userinfo,get_user_info,add_topic", listener);
        }
    }

    /**
     * 实际监听器
     * 用于监听登录接口回调结果
     * 并进行qq登录流程处理
     * @author 施立豪 
     * @time 2023/3/28
     */
    private class BaseUiListener implements IUiListener {

        protected void doComplete(JSONObject values) throws JSONException {
            //String token=values.getString("access_token");
            //唯一标识
            String openid=values.getString("openid");
            System.out.println(openid);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences LoginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                    //获得Editor 实例
                    SharedPreferences.Editor editor = LoginMessage.edit();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date=new Date();
                    //单例模式
                    Tracer tracer = GlobalTracer.get();
                    // 创建spann
                    Span span = tracer.buildSpan("qq登录流程").withTag("doComplete", "主追踪").start();
                    try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                        /**查询布隆过滤器redis
                         * 手机号是否存在
                         */
                        boolean IsPhoneExisted;
                        Span span1 = tracer.buildSpan("查询redis流程1").withTag("函数：doComplete", "子追踪").start();
                        try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                            IsPhoneExisted= UserOperate.IsPhoneExistBloom(openid, QqLoginActivity.this);
                        } catch (Exception e) {
                            TracingHelper.onError(e, span);
                            throw e;
                        } finally {
                            span.finish();
                        }
                        /**
                         * 账号已注册
                         */
                        if(IsPhoneExisted)
                        {
                            /**
                             * qq是否被封
                             */
                            boolean IsPhoneBaned;
                            Span span2 = tracer.buildSpan("查询redis流程2").withTag("函数：doComplete", "子追踪").start();
                            try (Scope ignored1 = tracer.scopeManager().activate(span2,true)) {
                                IsPhoneBaned=UserOperate.IsPhoneBanedBloom(openid,QqLoginActivity.this);
                            } catch (Exception e) {
                                TracingHelper.onError(e, span2);
                                throw e;
                            } finally {
                                span2.finish();
                            }
                            /**
                             * 被封处理
                             */
                            if(IsPhoneBaned){
                                System.out.println("账号已被封");
                                Log.i("e","账号被封");
                                return ;
                            }
                        }
                        /**
                         * 账号没注册-处理
                         */
                        else
                        {
                            /**
                             * 先插入mysql
                             */
                            {

                                boolean IsInserted;
                                String username= NameUtil.getTwoSurname();
                                Span span2 = tracer.buildSpan("查询mysql流程").withTag("函数：doComplete", "子追踪").start();
                                try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                    IsInserted=UserOperate.InsertByQq(openid,username,QqLoginActivity.this);
                                } catch (Exception e) {
                                    TracingHelper.onError(e, span);
                                    throw e;
                                } finally {
                                    span.finish();
                                }

                                if(IsInserted)
                                {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            /**
                                             * 查询用户mysql id
                                             * @time 2023/3/28
                                             */
                                            int result=-1;
                                            Span span2 = tracer.buildSpan("查询mysql流程").withTag("函数：doComplete", "子追踪").start();
                                            try (Scope ignored1 = tracer.scopeManager().activate(span2,true)) {
                                                result=UserOperate.GetQqId(openid,QqLoginActivity.this);
                                            } catch (Exception e) {
                                                TracingHelper.onError(e, span2);
                                                throw e;
                                            } finally {
                                                span2.finish();
                                            }
                                            if(result!=-1){
                                                /**
                                                 * 插入图数据库
                                                 * @time 2023/3/26
                                                 */
                                                boolean Isinserted;
                                                Span span3 = tracer.buildSpan("插入用户至图数据库").withTag("函数：doComplete", "子追踪").start();
                                                try (Scope ignored1 = tracer.scopeManager().activate(span3,true)) {
                                                    Isinserted=UserOperate.InsertUserToTu(username,result,QqLoginActivity.this);
                                                } catch (Exception e) {
                                                    TracingHelper.onError(e, span3);
                                                    throw e;
                                                }finally {
                                                    span3.finish();
                                                }}else
                                            {
                                                System.out.println("mysql的id查询失败！");
                                            }
                                        }
                                    }).start();
                                }
                                if(!IsInserted) System.out.println("插入mysql失败");
                                /**
                                 * 再插入redis
                                 */
                                else{Log.i("e","插入mysql成功");
                                    boolean IsInserted1;
                                    Span span3 = tracer.buildSpan("查询redis流程3").withTag("函数：doComplete", "子追踪").start();
                                    try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                        IsInserted1=UserOperate.InsertQqBloom(openid,QqLoginActivity.this);
                                    } catch (Exception e) {
                                        TracingHelper.onError(e, span);
                                        throw e;
                                    } finally {
                                        span.finish();
                                    }
                                    if(!IsInserted1) System.out.println("插入redis失败");
                                    else{Log.i("e","插入redis成功");}
                                }
                            }

                        }
                        /**
                         * 本地持久化+跳转到主页
                         */
                        {
                            String Time = format.format(date);
                            editor.putString("LoginData",Time);
                            editor.putString("QqId",openid);
                            System.out.println("newdate"+Time);
                            editor.apply();
                            Intent intent=new Intent(getApplicationContext(), ErHuoActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        TracingHelper.onError(e, span);
                        throw e;
                    } finally {
                        span.finish();
                    }
                }
            }).start();

        }

        //监听完成结果
        @Override
        public void onComplete(Object o) {
            try {
                doComplete((JSONObject) o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //接口调用出错
        @Override
        public void onError(UiError e) {
            System.out.println("onError:"+"code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }
        //取消调用
        @Override
        public void onCancel() {
            System.out.println("canceled");
        }
        //警告信息
        @Override
        public void onWarning(int i) {
            System.out.println("warning");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Tencent.handleResultData(data, listener);
        super.onActivityResult(requestCode, resultCode, data);

    }

}