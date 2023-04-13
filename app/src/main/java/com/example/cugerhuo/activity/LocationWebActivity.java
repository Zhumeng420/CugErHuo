package com.example.cugerhuo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocationClient;
import com.example.cugerhuo.R;



/**
 * 高德定位接口
 * @author 施立豪
 * @time 2023/4/12
 */
public class LocationWebActivity extends AppCompatActivity {
    /**
     * 地图视图
     */
    private WebView mapView;
    private AMapLocationClient mLocationClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView= (WebView) findViewById(R.id.webView);
        Context context=LocationWebActivity.this;
        AMapLocationClient.updatePrivacyAgree(context,true);
        AMapLocationClient.updatePrivacyShow(context,true,true);
        try {
            mLocationClient = new AMapLocationClient(context);//设置定位回调监听
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationClient.startAssistantLocation(mapView);
        //加载URL
        mapView.loadUrl("file:///android_asset/sdkLoc.html");
//设置webView参数和WebViewClient
        WebSettings webSettings = mapView.getSettings();
// 允许webview执行javaScript脚本
        webSettings.setJavaScriptEnabled(true);
//        mLocationClient.stopAssistantLocation();
        mapView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                        mapView.loadUrl("javascript:geo.getCurrentPosition()");

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                        mapView.loadUrl("javascript:geo.getCurrentPosition()");
            }
        });

        mapView.setWebChromeClient(new WebChromeClient() {
            // 处理javascript中的alert
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                return true;
            };

            // 处理javascript中的confirm
            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                return true;
            };

            // 处理定位权限请求
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
            @Override
            // 设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
                LocationWebActivity.this.getWindow().setFeatureInt(
                        Window.FEATURE_PROGRESS, newProgress * 100);
                super.onProgressChanged(view, newProgress);
            }

            // 设置应用程序的标题title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
//
    }
}
