package com.example.cugerhuo.login.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cugerhuo.activity.QqLoginActivity;
import com.example.cugerhuo.login.loginutils.AppUtils;
import com.example.cugerhuo.login.loginutils.CacheManage;
import com.example.cugerhuo.login.loginutils.NativeBackgroundAdapter;
import com.example.cugerhuo.R;
import com.mobile.auth.gatewayauth.AuthRegisterXmlConfig;
import com.mobile.auth.gatewayauth.AuthUIConfig;
import com.mobile.auth.gatewayauth.AuthUIControlClickListener;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;
import com.mobile.auth.gatewayauth.ResultCode;
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * xml文件方便预览
 * 可以通过addAuthRegisterXmlConfig一次性统一添加授权页的所有自定义view
 * MOVIE 一键登录界面布局与配置
 * @author 施立豪
 */
public class CustomMovConfig extends BaseUIConfig {
    private final String TAG = "CustomMovConfig";
    private Context mcontext;
    /**
     * 缓存管理
     */
    private CacheManage mCacheManage;
    /**
     *  线程池
     */
    private ExecutorService mThreadExecutor;
    /**
     * 本地背景适配器
     */
    private NativeBackgroundAdapter nativeBackgroundAdapter;
    /**
     * 构造函数，初始化缓存管理，线程池，背景适配器
     * @param activity  当前活动
     * @param authHelper    号码认证服务
     */
    public CustomMovConfig(Activity activity, PhoneNumberAuthHelper authHelper) {
        super(activity, authHelper);
        mCacheManage=new CacheManage(activity.getApplication());
        mThreadExecutor=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        /**
         * 背景视频文件
         * @author 唐小莉
         * @time 2023/3/14 22:41
         */

        nativeBackgroundAdapter =
                new NativeBackgroundAdapter(mCacheManage, mThreadExecutor, activity, "videoPath"
                        , "bg1.mp4");
    }
    /**
     * 协议授权/条款同意 选项
     */
    @Override
    public void configAuthPage() {
        mAuthHelper.setUIClickListener(new AuthUIControlClickListener() {
            @Override
            public void onClick(String code, Context context, String jsonString) {
                JSONObject jsonObj = null;
                try {
                    if(!TextUtils.isEmpty(jsonString)) {
                        jsonObj = new JSONObject(jsonString);
                    }
                } catch (JSONException e) {
                    jsonObj = new JSONObject();
                }
                switch (code) {
                    //点击授权页默认样式的返回按钮
                    case ResultCode.CODE_ERROR_USER_CANCEL:
                        Log.e(TAG, "点击了授权页默认返回按钮");
                        mAuthHelper.quitLoginPage();
                        mActivity.finish();
                        break;
                    //点击授权页默认样式的切换其他登录方式 会关闭授权页
                    //如果不希望关闭授权页那就setSwitchAccHidden(true)隐藏默认的  通过自定义view添加自己的
                    case ResultCode.CODE_ERROR_USER_SWITCH:
                        Log.e(TAG, "点击了授权页默认切换其他登录方式");
                        break;
                    //点击一键登录按钮会发出此回调
                    //当协议栏没有勾选时 点击按钮会有默认toast 如果不需要或者希望自定义内容 setLogBtnToastHidden(true)隐藏默认Toast
                    //通过此回调自己设置toast
                    case ResultCode.CODE_ERROR_USER_LOGIN_BTN:
                        if (!jsonObj.optBoolean("isChecked")) {
                            Toast.makeText(mContext, R.string.custom_toast, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //checkbox状态改变触发此回调
                    case ResultCode.CODE_ERROR_USER_CHECKBOX:
                        Log.e(TAG, "checkbox状态变为" + jsonObj.optBoolean("isChecked"));
                        break;
                    //点击协议栏触发此回调
                    case ResultCode.CODE_ERROR_USER_PROTOCOL_CONTROL:
                        Log.e(TAG, "点击协议，" + "name: " + jsonObj.optString("name") + ", url: " + jsonObj.optString("url"));
                        break;
                    //用户调用userControlAuthPageCancel后左上角返回按钮及物理返回键交由sdk接入方控制
                    case ResultCode.CODE_ERROR_USER_CONTROL_CANCEL_BYBTN:
                        Log.e(TAG, "用户调用userControlAuthPageCancel后使用左上角返回按钮返回交由sdk接入方控制");
                        mAuthHelper.quitLoginPage();
                        mActivity.finish();
                        break;
                    //用户调用userControlAuthPageCancel后物理返回键交由sdk接入方控制
                    case ResultCode.CODE_ERROR_USER_CONTROL_CANCEL_BYKEY:
                        Log.e(TAG, "用户调用userControlAuthPageCancel后使用物理返回键返回交由sdk接入方控制");
                        mAuthHelper.quitLoginPage();
                        mActivity.finish();
                        break;

                    default:
                        break;

                }

            }
        });
        mAuthHelper.removeAuthRegisterXmlConfig();
        mAuthHelper.removeAuthRegisterViewConfig();
        int authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
        }
        updateScreenSize(authPageOrientation);
        //sdk默认控件的区域是marginTop50dp
        int designHeight = mScreenHeightDp - 50;
        int unit = designHeight / 10;
        mAuthHelper.addAuthRegisterXmlConfig(new AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.authsdk_widget_custom_layout, new AbstractPnsViewDelegate() {
                    @Override
                    public void onViewCreated(View view) {
                        final FrameLayout flyContainer = view.findViewById(R.id.fly_container);
                        nativeBackgroundAdapter.solveView(flyContainer, "#FF8C00");
                    }
                })
                .build());
        /**
         * 添加第三方登录控件
         * @author 施立豪
         * @time 2023/3/27
         */
        mAuthHelper.addAuthRegisterXmlConfig(new AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.custom_land_dialog, new AbstractPnsViewDelegate() {
                    @Override
                    public void onViewCreated(View view) {

                        int iconTopMargin = AppUtils.dp2px(getContext(),  mScreenHeightDp-100);
                        View iconContainer = findViewById(R.id.container_icon);
                        RelativeLayout.LayoutParams iconLayout = (RelativeLayout.LayoutParams) iconContainer.getLayoutParams();
                        //设置子界面的高
                        iconLayout.topMargin = iconTopMargin;
                        //设置子界面的宽
                        iconLayout.width = AppUtils.dp2px(getContext(),  mScreenWidthDp/2);
                        findViewById(R.id.qq).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * 跳转到qq登录
                                 * @author 施立豪
                                 * @time 2023/3/27
                                 */

                                System.out.println("qq登录");
                                Intent intent=new Intent();
                                intent.setClass(mContext, QqLoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                })
                .build());
        mAuthHelper.setAuthUIConfig(new AuthUIConfig.Builder()
                .setAppPrivacyOne("《自定义隐私协议》", "https://test.h5.app.tbmao.com/user")
                .setAppPrivacyTwo("《百度》", "https://www.baidu.com")
                .setAppPrivacyColor(Color.GRAY, Color.parseColor("#000000"))
                .setNavHidden(true)
                .setLogoHidden(true)
                .setSloganHidden(true)
                .setSwitchAccHidden(true)
                .setPrivacyState(false)
                .setCheckboxHidden(true)
                .setLightColor(true)
                .setNumFieldOffsetY(unit * 6)
                .setLogBtnOffsetY(unit * 7)
                .setWebViewStatusBarColor(Color.TRANSPARENT)
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setWebNavTextSizeDp(20)
                .setNumberSizeDp(20)
                .setNumberColor(Color.parseColor("#000000"))
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setPageBackgroundPath("page_background_color")
                .setLogoImgPath("mytel_app_launcher")
                .setLogBtnBackgroundPath("login_btn_bg")
                .setScreenOrientation(authPageOrientation)
                .create());
    }
}