package com.example.cugerhuo.login.login;

import static com.example.cugerhuo.login.utils.MockRequest.getPhoneNumber;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.SetGlobalIDandUrl;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.ErHuoActivity;
import com.example.cugerhuo.activity.QqLoginActivity;
import com.example.cugerhuo.login.config.BaseUIConfig;
import com.example.cugerhuo.login.loginutils.BuildConfig;
import com.example.cugerhuo.login.loginutils.Constant;
import com.example.cugerhuo.login.utils.ExecutorManager;
import com.example.cugerhuo.tools.InitChatAccount;
import com.example.cugerhuo.tools.NameUtil;
import com.example.cugerhuo.tools.TracingHelper;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.ResultCode;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;

import java.util.Date;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
/**
 * 进CUG贰货一键的场景
 * @author 施立豪
 * @time 2023/3/5
 */

/**
 * 网络工具包okhttp:
 * @link https://juejin.cn/post/7068162792154464264
 * 使用该工具向服务端请求对应的手机号码
 * @author 朱萌
 * @time 2023/3/5 17:32
 */
public class OneKeyLoginActivity extends Activity {
    /**
     * 类名
     */
    private static final String TAG = OneKeyLoginActivity.class.getSimpleName();
    /**
     * 用于显示登录成功后的Token文本框
     */
    private EditText mTvResult;
    /**
     * 获取号码认证服务示例，此实例为单例，获取多次为同一对象
     */
    private PhoneNumberAuthHelper mPhoneNumberAuthHelper;
    private ImageView qqlogin;
    /**
     * 监听器--Token返回结果
     */
    private TokenResultListener mTokenResultListener;
    /**
     *  登录进度对话框
     */
    private ProgressDialog mProgressDialog;
    /**
     * 画面显示类型  0：GIF  1：视频
     */
    private int uiType;
    /**
     * 登录画面配置器
     */
    private BaseUIConfig uiConfig;

    /**
     * 创建时加载
     * @param savedInstanceState 已保存的实例状态
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * qq登录初始化
         */

        /**
         * 通过intent得到当前显示主题为GIF或视频，设置相应布局和号码认证服务
         */
        uiType = getIntent().getIntExtra(Constant.THEME_KEY, -1);
        setContentView(R.layout.activity_login);
        mTvResult = findViewById(R.id.tv_result);
        sdkInit(BuildConfig.AUTH_SECRET);
        uiConfig = BaseUIConfig.init(uiType, this, mPhoneNumberAuthHelper);
        oneKeyLogin();
        /**
         * 增加EditTxt的复制功能
         */
        mTvResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTvResult.setTextIsSelectable(true);
                mTvResult.setSelectAllOnFocus(true);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                /**创建普通字符型ClipData
                 *
                 */
                ClipData mClipData = ClipData.newPlainText("Label", mTvResult.getText());
                /**
                 * 将ClipData内容放到系统剪贴板里。
                 */

                cm.setPrimaryClip(mClipData);
                Toast.makeText(OneKeyLoginActivity.this,"登录token已复制",Toast.LENGTH_SHORT).show();
                return false;
            }

        });
    }

    /**
     * 初始化sdk
     * @param secretInfo
     */
    public void sdkInit(String secretInfo) {
        mTokenResultListener = new TokenResultListener() {
            /**
             * 获取token成功处理
             * @param s token信息
             */
            @Override
            public void onTokenSuccess(String s) {
                hideLoadingDialog();
                Log.e(TAG, "获取token成功：" + s);
                TokenRet tokenRet = null;
                try {
                    tokenRet = TokenRet.fromJson(s);
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS.equals(tokenRet.getCode())) {
                        Log.i("TAG", "唤起授权页成功：" + s);
                    }

                    if (ResultCode.CODE_SUCCESS.equals(tokenRet.getCode())) {
                        Log.i("TAG", "获取token成功：" + s);
                        getResultWithToken(tokenRet.getToken());
                        mPhoneNumberAuthHelper.setAuthListener(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 获取token失败处理
             * @param s 失败信息
             */
            @Override
            public void onTokenFailed(String s) {

                Log.e(TAG, "获取token失败：" + s);
                hideLoadingDialog();
                mPhoneNumberAuthHelper.hideLoginLoading();
                TokenRet tokenRet = null;
                try {
                    tokenRet = TokenRet.fromJson(s);
                    if (ResultCode.CODE_ERROR_USER_CANCEL.equals(tokenRet.getCode())) {
                        /**
                         * 必须登录 否则直接退出app的场景
                         */
                    } else {
                        Toast.makeText(getApplicationContext(), "一键登录失败切换到其他登录方式", Toast.LENGTH_SHORT).show();
                        Intent pIntent = new Intent(OneKeyLoginActivity.this, QqLoginActivity.class);
                        startActivityForResult(pIntent, 1002);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mPhoneNumberAuthHelper.quitLoginPage();
                mPhoneNumberAuthHelper.setAuthListener(null);
            }
        };
        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(this, mTokenResultListener);
        mPhoneNumberAuthHelper.getReporter().setLoggerEnable(true);
        mPhoneNumberAuthHelper.setAuthSDKInfo(secretInfo);
    }
    /**
     * 进入app就需要登录的场景使用
     */
    private void oneKeyLogin() {
        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(getApplicationContext(), mTokenResultListener);
        mPhoneNumberAuthHelper.checkEnvAvailable();
        uiConfig.configAuthPage();
        //用户控制返回键及左上角返回按钮效果
        mPhoneNumberAuthHelper.userControlAuthPageCancel();
        //用户禁用utdid
        //mPhoneNumberAuthHelper.prohibitUseUtdid();
        //授权页是否跟随系统深色模式
        mPhoneNumberAuthHelper.setAuthPageUseDayLight(true);
        //授权页物理返回键禁用
        //mPhoneNumberAuthHelper.closeAuthPageReturnBack(true);
        //横屏水滴屏全屏适配
        mPhoneNumberAuthHelper.keepAuthPageLandscapeFullSreen(true);
        //授权页扩大协议按钮选择范围至我已阅读并同意
        //mPhoneNumberAuthHelper.expandAuthPageCheckedScope(true);
        getLoginToken(5000);
    }

    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    public void getLoginToken(int timeout) {
        mPhoneNumberAuthHelper.getLoginToken(this, timeout);
        showLoadingDialog("正在唤起授权页");
    }

    /**
     * 得到结果中的token
     * @param token
     */
    public void getResultWithToken(final String token) {
        ExecutorManager.run(new Runnable() {
            @Override
            public void run() {

                /**
                 * 查询本地存储
                 */
                SharedPreferences loginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                //获得Editor 实例
                SharedPreferences.Editor editor = loginMessage.edit();
                //以key-value形式保存数据
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                /**
                 * 登录信息过期重新登陆
                 */
                final String phoneNumber = getPhoneNumber(token,OneKeyLoginActivity.this);
                System.out.println(phoneNumber);
                OneKeyLoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //单例模式
                                Tracer tracer = GlobalTracer.get();
                                // 创建spann
                                Span span = tracer.buildSpan("登录流程").withTag("getResultWithToken", "主追踪").start();
                                try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                                    /**查询布隆过滤器redis
                                     * 手机号是否存在
                                     */
                                    boolean isPhoneExisted;
                                    Span span1 = tracer.buildSpan("查询redis流程1").withTag("函数：getResultWithToken", "子追踪").start();
                                    try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                        isPhoneExisted= UserOperate.isPhoneExistBloom(phoneNumber,OneKeyLoginActivity.this);
                                    } catch (Exception e) {
                                        TracingHelper.onError(e, span);
                                        throw e;
                                    } finally {
                                        span.finish();
                                    }
                                    /**
                                     * 账号已注册
                                     */
                                    if(isPhoneExisted)
                                    {
                                        /**
                                         * 手机号是否被封
                                         */
                                        boolean isPhoneBaned;
                                        Span span2 = tracer.buildSpan("查询redis流程2").withTag("函数：getResultWithToken", "子追踪").start();
                                        try (Scope ignored1 = tracer.scopeManager().activate(span2,true)) {
                                            isPhoneBaned=UserOperate.isPhoneBanedBloom(phoneNumber,OneKeyLoginActivity.this);
                                            SetGlobalIDandUrl.setByPhone(phoneNumber, OneKeyLoginActivity.this);

                                        } catch (Exception e) {
                                            TracingHelper.onError(e, span2);
                                            throw e;
                                        } finally {
                                            span2.finish();
                                        }
                                        /**
                                         * 被封处理
                                         */
                                        if(isPhoneBaned){
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
                                            Span span2 = tracer.buildSpan("查询mysql流程").withTag("函数：getResultWithToken", "子追踪").start();
                                            try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {

                                                isInserted=UserOperate.insertByPhone(phoneNumber,username,OneKeyLoginActivity.this);

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
                                                         * @time 2023/3/26
                                                         */
                                                        int result=-1;
                                                        Span span2 = tracer.buildSpan("查询mysql流程").withTag("函数：getResultWithToken", "子追踪").start();
                                                        try (Scope ignored1 = tracer.scopeManager().activate(span2,true)) {
                                                            result=UserOperate.getId(phoneNumber,OneKeyLoginActivity.this);
                                                        } catch (Exception e) {
                                                            TracingHelper.onError(e, span2);
                                                            throw e;
                                                        } finally {
                                                            span2.finish();
                                                        }
                                                        if(result!=-1){
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
                                                        boolean isinserted;
                                                        Span span3 = tracer.buildSpan("插入用户至图数据库").withTag("函数：getResultWithToken", "子追踪").start();
                                                        try (Scope ignored1 = tracer.scopeManager().activate(span3,true)) {
                                                                isinserted=UserOperate.insertUserToTu(username,result,OneKeyLoginActivity.this);
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
                                                            boolean isinserted1;
                                                            Span span4 = tracer.buildSpan("手机注册插入用户至用户资料").withTag("函数：doComplete", "子追踪").start();
                                                            try (Scope ignored1 = tracer.scopeManager().activate(span3,true)) {
                                                                isinserted1= UserInfoOperate.insertUser(result,username, OneKeyLoginActivity.this);
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
                                                            SetGlobalIDandUrl.setByPhone(phoneNumber, OneKeyLoginActivity.this);

                                                        }
                                                        else
                                                        {
                                                            System.out.println("mysql的id查询失败！");
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
                                                Span span3 = tracer.buildSpan("查询redis流程3").withTag("函数：getResultWithToken", "子追踪").start();
                                                try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                                    isInserted1=UserOperate.insertPhoneBloom(phoneNumber,OneKeyLoginActivity.this);
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
                                     *
                                     */
                                    {                                        /**
                                         * 本地存储
                                         */
                                        String time = format.format(date);
                                        editor.putString("LoginData",time);
                                        editor.putString("PhoneNumber",phoneNumber);
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

                        mTvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
                        mPhoneNumberAuthHelper.quitLoginPage();
                    }
                });
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {
            if (resultCode == 1) {
                mTvResult.setText("登陆成功：" + data.getStringExtra("result"));
                mTvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
            } else {
                //模拟的是必须登录 否则直接退出app的场景
                finish();
            }
        }
    }


    /**
     * 登录进度加载展示
     * @param hint
     */
    public void showLoadingDialog(String hint) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        mProgressDialog.setMessage(hint);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    /**
     * 隐藏进度加载
     */
    public void hideLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 恢复页面
     */
    @Override
    protected void onResume() {
        super.onResume();
        uiConfig.onResume();
    }



}
