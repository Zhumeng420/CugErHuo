package com.example.cugerhuo.login.utils;

import android.content.Context;
import android.util.Log;

import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.rsa.RsaClientUtils;
import com.example.cugerhuo.tools.rsa.RsaClientUtilsImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    public static String getPhoneNumber(String token1, Context context) {
        String result = "";
        try {

            /**
             * 使用okhttp调用获取手机号码接口
             * @author 朱萌，施立豪
             * @time 2023/3/13
             */
            OkHttpClient okHttpClient = new OkHttpClient();
            /**
             * 获取XML文本
             */
            String ip=context.getString(R.string.ip);
            String router=context.getString(R.string.GetUserPhone);
            String token=context.getString(R.string.Token);
            String publicKey=context.getString(R.string.PublicKey);
            /**
             * 获取公钥
             */
            RsaClientUtils rsaClientUtils = new RsaClientUtilsImpl();
            rsaClientUtils.generateKey();
            String publicKey1 = rsaClientUtils.getPublicKey();
            /**
             * 发送请求
             */
            Map<String,String> map=new HashMap<>();
            String url="http://"+ip+"/"+router;
            FormBody.Builder builder = new FormBody.Builder();
            builder.add(token,token1);
            builder.add(publicKey,publicKey1);
            //循环form表单，将表单内容添加到form builder中
            //构建formBody，将其传入Request请求中
            FormBody body = builder.build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = null;
            String str=null;
            try {
                response = okHttpClient.newCall(request).execute();
                byte [] a=response.body().bytes();
                if(!Objects.equals(str, "")){
                // 客户端利用私钥将密文解密
                result = rsaClientUtils.decrypt(a, rsaClientUtils.getPrivateKey());
                result=result.substring(result.length()-11);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
