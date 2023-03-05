package com.example.cugerhuo.config;

import static com.example.cugerhuo.loginUtils.AppUtils.dp2px;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cugerhuo.loginUtils.AppUtils;
import com.example.cugerhuo.loginUtils.Constant;
import com.example.cugerhuo.R;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;

/**
 * 一键登录页面布局与配置
 * @link  https://help.aliyun.com/document_detail/144231.htm?spm=a2c4g.11186623.0.0.57aa60b7QSBxBF#topic20685
 * @author 施立豪
 *
 */
public abstract class BaseUIConfig {
    public Activity mActivity;
    public Context mContext;
    /**
     * 号码认证服务
     */
    public PhoneNumberAuthHelper mAuthHelper;
    /**
     * 屏幕尺寸
     */
    public int mScreenWidthDp;
    public int mScreenHeightDp;
    /**
     * 布局类型初始化，选择GIF或者MOVIE
     * @param type      类型变量 0：gif ，1：movie
     * @param activity  当前activity
     * @param authHelper    号码认证服务
     * @return  返回所选类型相应的配置
     */
    public static BaseUIConfig init(int type, Activity activity, PhoneNumberAuthHelper authHelper) {
        switch (type) {
            case Constant.CUSTOM_GIF:
                return new CustomGifConfig(activity, authHelper);
            case Constant.CUSTOM_MOV:
                return new CustomMovConfig(activity, authHelper);
            default:
                return null;
        }
    }

    public BaseUIConfig(Activity activity, PhoneNumberAuthHelper authHelper) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mAuthHelper = authHelper;
    }

    /**
     * 初始化短信登录的视图
     * @param marginTop
     * @return
     */
    protected View initSwitchView(int marginTop) {
        TextView switchTV = new TextView(mActivity);
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dp2px(mActivity, 50));
        //一键登录按钮默认marginTop 270dp
        mLayoutParams.setMargins(0, dp2px(mContext, marginTop), 0, 0);
        mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        switchTV.setText(R.string.switch_msg);
        switchTV.setTextColor(Color.BLACK);
        switchTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.0F);
        switchTV.setLayoutParams(mLayoutParams);
        return switchTV;
    }

    /**
     * 根据类型，更新屏幕尺寸
     * @param authPageScreenOrientation
     */
    protected void updateScreenSize(int authPageScreenOrientation) {
        int screenHeightDp = AppUtils.px2dp(mContext, AppUtils.getPhoneHeightPixels(mContext));
        int screenWidthDp = AppUtils.px2dp(mContext, AppUtils.getPhoneWidthPixels(mContext));
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        if (authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_BEHIND) {
            authPageScreenOrientation = mActivity.getRequestedOrientation();
        }
        if (authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE) {
            rotation = Surface.ROTATION_90;
        } else if (authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                || authPageScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT) {
            rotation = Surface.ROTATION_180;
        }
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                mScreenWidthDp = screenWidthDp;
                mScreenHeightDp = screenHeightDp;
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                mScreenWidthDp = screenHeightDp;
                mScreenHeightDp = screenWidthDp;
                break;
            default:
                break;
        }
    }

    public abstract void configAuthPage();

    /**
     *  在横屏APP弹竖屏一键登录页面或者竖屏APP弹横屏授权页时处理特殊逻辑
     *  Android8.0只能启动SCREEN_ORIENTATION_BEHIND模式的Activity
     */
    public void onResume() {

    }
}
