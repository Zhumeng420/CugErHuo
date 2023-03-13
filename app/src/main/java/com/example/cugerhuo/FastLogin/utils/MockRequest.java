package com.example.cugerhuo.FastLogin.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * token具体请求（待实现），配置阿里云token服务
 * @author 施立豪
 */
public class MockRequest {
    private static final String TAG = MockRequest.class.getSimpleName();


    /**
     * 开发者自己app的服务端对接阿里号码认证，并提供接口给app调用
     * 此处调用服务接口将本机号码校验token以及手机传递过去
     * 根据返回结果进行处理
     * @return 服务端校验结果
     */
    public static String verifyNumber(String token, String phoneNumber) {
        try {
            //模拟网络请求
            Log.i(TAG, "进行本机号码校验：" + "token: " + token + ", phoneNumber: " + phoneNumber);
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "本机号码校验成功";
    }

    /**
     * 开发者自己app的服务端对接阿里号码认证，并提供接口给app调用
     * 1、调用app服务端接口将一键登录token发送过去
     * 2、app服务端拿到token调用阿里号码认证服务端换号接口，获取手机号
     * 3、app服务端拿到手机号帮用户完成注册以及登录的逻辑，返回账户信息给app
     * @return 账户信息
     */
    public static String getPhoneNumber(String token) {
        String result = "";
        try {
            /**
             * 使用okhttp调用获取手机号码接口
             * @author 朱萌
             * @time 2023/3/13
             */
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url("http://123.249.120.9:8082/phonenumber/getnumber"
                    +"&token="+token).get().build();
            Response response = null;
            String str=null;
            try {
                response = okHttpClient.newCall(request).execute();
                str = Objects.requireNonNull(response.body()).string();
                System.out.println(str);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //模拟网络请求
            Log.i(TAG, "一键登录换号：" + "token: " + token );
            Thread.sleep(500);
            JSONObject pJSONObject = new JSONObject();
            pJSONObject.put("account", UUID.randomUUID().toString());
            pJSONObject.put("phoneNumber", str);
            pJSONObject.put("token", token);
            result = pJSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
