package com.example.cugerhuo.tools;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * @Description:注册云信
 * @Data 2023.4.28
 * @author 施立豪
 * @Param id 注册id
 */
public class InitChatAccount {

    public static final String addUser(String id){
        String result=null;
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
        String appKey = "c45d8e893b6e4d7b2fab77a68dfe8ad8";
        String appSecret = "26094692fd7c";
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//获取CheckSum

// 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("accid", id)
                .add("token","123456")
                .build();

// 构建请求
        Request request = new Request.Builder()
                .url(url)
                .addHeader("AppKey", appKey)
                .addHeader("Nonce", nonce)
                .addHeader("CurTime", curTime)
                .addHeader("CheckSum", checkSum)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .post(requestBody)
                .build();

// 发起请求

        try (Response response = client.newCall(request).execute()) {
            result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
