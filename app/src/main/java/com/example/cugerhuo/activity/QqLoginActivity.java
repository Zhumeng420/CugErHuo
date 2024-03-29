package com.example.cugerhuo.activity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.access.SetGlobalIDandUrl;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.tools.InitChatAccount;
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
     *  监听接口结果
     */
    BaseUiListener listener;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listener=new BaseUiListener();
    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    // 其中APP_ID是分配给第三方应用的appid，类型为String。
    // 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值
        /**
         * 尝试修复qq因版本问题无法登录
         * @author 施立豪
         * @Time 2023/4/3
         */
        mTencent = Tencent.createInstance("102046332", this.getApplicationContext());
        /**
         * 获取授权
         */
        Tencent.setIsPermissionGranted(true);
        /**
         * 设置监听器
         */
        login();
        //LoginButton.setOnClickListener(this::login);
    }
    /**
     * 调用登录接口
     * @param当前页面
     * @time 2023/3/27
     */
    public void login()
    {
        mTencent = Tencent.createInstance("102046332", this.getApplicationContext());
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
                    SharedPreferences loginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                    //获得Editor 实例
                    SharedPreferences.Editor editor = loginMessage.edit();
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
                        boolean isQqExisted;
                        Span span1 = tracer.buildSpan("查询redis流程1").withTag("函数：doComplete", "子追踪").start();
                        try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                            isQqExisted= UserOperate.isQqExistBloom(openid, QqLoginActivity.this);
                        } catch (Exception e) {
                            TracingHelper.onError(e, span);
                            throw e;
                        } finally {
                            span.finish();
                        }
                        /**
                         * 账号已注册
                         */
                        if(isQqExisted)
                        {
                            /**
                             * qq是否被封
                             */
                            boolean isQqBaned;
                            Span span2 = tracer.buildSpan("查询redis流程2").withTag("函数：doComplete", "子追踪").start();
                            try (Scope ignored1 = tracer.scopeManager().activate(span2,true)) {
                                isQqBaned=UserOperate.isQqBanedBloom(openid,QqLoginActivity.this);
                            } catch (Exception e) {
                                TracingHelper.onError(e, span2);
                                throw e;
                            } finally {
                                span2.finish();
                            }
                            SetGlobalIDandUrl.setByQq(openid,QqLoginActivity.this);

                            /**
                             * 被封处理
                             */
                            if(isQqBaned){
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
                                boolean isInserted;
                                String username= NameUtil.getTwoSurname();
                                Span span2 = tracer.buildSpan("查询mysql流程").withTag("函数：doComplete", "子追踪").start();
                                try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                    isInserted=UserOperate.insertByQq(openid,username,QqLoginActivity.this);
                                } catch (Exception e) {
                                    TracingHelper.onError(e, span);
                                    throw e;
                                } finally {
                                    span.finish();
                                }
                                if(isInserted)
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
                                                result=UserOperate.getQqId(openid,QqLoginActivity.this);
                                            } catch (Exception e) {
                                                TracingHelper.onError(e, span2);
                                                throw e;
                                            } finally {
                                                span2.finish();
                                            }
                                            if(result!=-1){
                                                /**
                                                 * 保存ID
                                                 */
                                                UserInfo.setid(result);

                                                /**
                                                 * 注册云信
                                                 * @time 2023/4/28
                                                 */
                                                Span span31 = tracer.buildSpan("注册云信").withTag("函数：doComplete", "子追踪").start();
                                                try (Scope ignored1 = tracer.scopeManager().activate(span31,true)) {
                                                    InitChatAccount.addUser("CUGerhuo"+String.valueOf(result));                                                } catch (Exception e) {
                                                    TracingHelper.onError(e, span31);
                                                    throw e;
                                                }finally {
                                                    span31.finish();
                                                }

                                                /**
                                                 * 插入图数据库
                                                 * @time 2023/3/26
                                                 */
                                                boolean isInserted1;
                                                Span span3 = tracer.buildSpan("插入用户至图数据库").withTag("函数：doComplete", "子追踪").start();
                                                try (Scope ignored1 = tracer.scopeManager().activate(span3,true)) {
                                                    isInserted1 =UserOperate.insertUserToTu(username,result,QqLoginActivity.this);
                                                } catch (Exception e) {
                                                    TracingHelper.onError(e, span3);
                                                    throw e;
                                                }finally {
                                                    span3.finish();
                                                }
                                                /**
                                                 * 插入用户资料表
                                                 * @time 2023/4/9
                                                 */
                                                Span span4 = tracer.buildSpan("qq注册插入用户至用户资料").withTag("函数：doComplete", "子追踪").start();
                                                try (Scope ignored1 = tracer.scopeManager().activate(span3,true)) {
                                                    isInserted1 = UserInfoOperate.insertUser(result,username,QqLoginActivity.this);
                                                } catch (Exception e) {
                                                    TracingHelper.onError(e, span3);
                                                    throw e;
                                                }finally {
                                                    span3.finish();
                                                }
                                                /**
                                                 * 初始化全局变量
                                                 * @author 施立豪
                                                 * @time 2023/4/9
                                                 */
                                                SetGlobalIDandUrl.setByQq(openid,QqLoginActivity.this);
                                            }
                                            else
                                            {
                                                Log.e(TAG,"mysql的id查询失败！");
                                            }
                                        }
                                    }).start();
                                }
                                if(!isInserted) {System.out.println("插入mysql失败");}
                                /**
                                 * 再插入redis
                                 */
                                else{Log.i("e","插入mysql成功");
                                    boolean isInserted1;
                                    Span span3 = tracer.buildSpan("查询redis流程3").withTag("函数：doComplete", "子追踪").start();
                                    try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                        isInserted1=UserOperate.insertQqBloom(openid,QqLoginActivity.this);
                                    } catch (Exception e) {
                                        TracingHelper.onError(e, span);
                                        throw e;
                                    } finally {
                                        span.finish();
                                    }
                                    if(!isInserted1){ System.out.println("插入redis失败");}
                                    else{Log.i("e","插入redis成功");}
                                }
                            }

                        }
                        /**
                         * 初始化全局变量，本地持久化+跳转到主页
                         */
                        {
                            /**
                             * 本地存储
                             */
                            String time = format.format(date);
                            editor.putString("QqLoginData",time);
                            editor.putString("QqId",openid);
                            System.out.println("newdate"+time);
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
        finish();
    }

}