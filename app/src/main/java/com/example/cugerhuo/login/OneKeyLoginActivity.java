package com.example.cugerhuo.login;

import static com.example.cugerhuo.loginUtils.Constant.THEME_KEY;
import static com.example.cugerhuo.loginUtils.Constant.THEME_KEY;
import static com.example.cugerhuo.utils.MockRequest.getPhoneNumber;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cugerhuo.loginUtils.BuildConfig;
import com.example.cugerhuo.loginUtils.MessageActivity;
import com.example.cugerhuo.R;
import com.example.cugerhuo.config.BaseUIConfig;
import com.example.cugerhuo.utils.ExecutorManager;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.ResultCode;
import com.mobile.auth.gatewayauth.TokenResultListener;
import com.mobile.auth.gatewayauth.model.TokenRet;

/**
 * 进CUG贰货一键的场景
 * @author 施立豪
 * @time 2023/3/5
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
    private int mUIType;
    /**
     * 登录画面配置器
     */
    private BaseUIConfig mUIConfig;

    /**
     * 创建时加载
     * @param savedInstanceState 已保存的实例状态
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 通过intent得到当前显示主题为GIF或视频，设置相应布局和号码认证服务
         */
        mUIType = getIntent().getIntExtra(THEME_KEY, -1);
        setContentView(R.layout.activity_login);
        mTvResult = findViewById(R.id.tv_result);
        sdkInit(BuildConfig.AUTH_SECRET);
        mUIConfig = BaseUIConfig.init(mUIType, this, mPhoneNumberAuthHelper);
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
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "一键登录失败切换到其他登录方式", Toast.LENGTH_SHORT).show();
                        Intent pIntent = new Intent(OneKeyLoginActivity.this, MessageActivity.class);
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
        mUIConfig.configAuthPage();
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
                final String result = getPhoneNumber(token);
                OneKeyLoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResult.setText("登陆成功：" + result);
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
        mUIConfig.onResume();
    }
}
