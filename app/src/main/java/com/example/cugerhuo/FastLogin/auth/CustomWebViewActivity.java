package com.example.cugerhuo.FastLogin.auth;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.cugerhuo.R;
import com.mobile.auth.gatewayauth.Constant;

/**
 * 网页视图展示，用于打开登录页面的协议，网页等
 * @author 施立豪
 */
public class CustomWebViewActivity extends Activity {
   private WebView mWebView;
   private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_web);
        String mUrl = getIntent().getStringExtra(Constant.PROTOCOL_WEB_VIEW_URL);
        String mName = getIntent().getStringExtra(Constant.PROTOCOL_WEB_VIEW_NAME);
        setRequestedOrientation(
            getIntent().getIntExtra(Constant.PROTOCOL_WEB_VIEW_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
        mWebView = findViewById(R.id.webView);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setSubtitle(mName);
        initWebView();
        mWebView.loadUrl(mUrl);
    }

    private void initWebView() {
        WebSettings wvSettings = mWebView.getSettings();
        // 是否阻止网络图像
        wvSettings.setBlockNetworkImage(false);
        // 是否阻止网络请求
        wvSettings.setBlockNetworkLoads(false);
        // 是否加载JS
        wvSettings.setJavaScriptEnabled(true);
        wvSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //覆盖方式启动缓存
        wvSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 使用广泛视窗
        wvSettings.setUseWideViewPort(true);
        wvSettings.setLoadWithOverviewMode(true);
        wvSettings.setDomStorageEnabled(true);
        //是否支持缩放
        wvSettings.setBuiltInZoomControls(false);
        wvSettings.setSupportZoom(false);
        //不显示缩放按钮
        wvSettings.setDisplayZoomControls(false);
        wvSettings.setAllowFileAccess(true);
        wvSettings.setDatabaseEnabled(true);
        mWebView.setVerticalScrollbarOverlay(false); //不出现指定的垂直滚动条有叠加样式
        wvSettings.setUseWideViewPort(true);//设定支持viewport
        wvSettings.setBuiltInZoomControls(true);//设置出现缩放工具
        wvSettings.setDisplayZoomControls(false);//设置缩放工具隐藏
        wvSettings.setSupportZoom(true);//设定支持缩放
        //缓存相关
        wvSettings.setAppCacheEnabled(true);
        wvSettings.setDomStorageEnabled(true);
        wvSettings.setDatabaseEnabled(true);
    }
}
