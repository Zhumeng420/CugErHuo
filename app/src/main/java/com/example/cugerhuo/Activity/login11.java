package com.example.cugerhuo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import io.netty.channel.ConnectTimeoutException;

public class login11 extends AppCompatActivity {
    Tencent mTencent;
    Button mbu;
    BaseUiListener listener;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listener=new BaseUiListener();
        setContentView(R.layout.qq_login);
        mbu=findViewById(R.id.lbu);
// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
// 其中APP_ID是分配给第三方应用的appid，类型为String。
// 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值

        mTencent = Tencent.createInstance("100330589", this.getApplicationContext(),"com.example.cugerhuo.fileprovider");
        Tencent.setIsPermissionGranted(true);
        mbu.setOnClickListener(this::login);
// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
// 初始化视图

    }

    public void login(View view)
    {
        mTencent = Tencent.createInstance("100330589", this.getApplicationContext(),"com.example.cugerhuo.fileprovider");
        Tencent.setIsPermissionGranted(true);

        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "get_simple_userinfo,get_user_info,add_topic", listener);

        }
    }
    private class BaseUiListener implements IUiListener {

        protected void doComplete(JSONObject values) {
            System.out.println("resutl"+values.toString());
        }

        @Override
        public void onComplete(Object o) {
            doComplete((JSONObject) o);
        }

        @Override
        public void onError(UiError e) {
            System.out.println("onError:"+"code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }
        @Override
        public void onCancel() {
            System.out.println("canceled");
        }

        @Override
        public void onWarning(int i) {
            System.out.println("warning");
        }
    }
    private class BaseApiListener implements IRequestListener {

        public void onComplete(final JSONObject response, Object state) {
            System.out.println(("IRequestListener.onComplete:"+response.toString()));
            doComplete(response, state);
        }
        protected void doComplete(JSONObject response, Object state) {
        }

        public void onIOException(final IOException e, Object state) {
            System.out.println(("IRequestListener.onIOException:"+e.getMessage()));
        }

        public void onMalformedURLException(final MalformedURLException e,
                                            Object state) {
            System.out.println(("IRequestListener.onMalformedURLException"+e.toString()));
        }

        public void onJSONException(final JSONException e, Object state) {
            System.out.println(("IRequestListener.onJSONException:"+ e.getMessage()));
        }

        public void onConnectTimeoutException(ConnectTimeoutException arg0,
                                              Object arg1) {
// TODO Auto-generated method stub
        }

        public void onSocketTimeoutException(SocketTimeoutException arg0,
                                             Object arg1) {
// TODO Auto-generated method stub
        }
        //1.4版本中IRequestListener 新增两个异常

        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e, Object state){
// 当前网络不可用时触发此异常
        }

        public void onHttpStatusException(HttpUtils.HttpStatusException e, Object state) {
// http请求返回码非200时触发此异常
        }
        public void onUnknowException(Exception e, Object state) {
// 出现未知错误时会触发此异常
        }

        @Override
        public void onComplete(JSONObject jsonObject) {

            System.out.println(jsonObject.toString());
        }

        @Override
        public void onIOException(IOException e) {

        }

        @Override
        public void onMalformedURLException(MalformedURLException e) {

        }

        @Override
        public void onJSONException(JSONException e) {

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e) {

        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {

        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e) {

        }

        @Override
        public void onUnknowException(Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}