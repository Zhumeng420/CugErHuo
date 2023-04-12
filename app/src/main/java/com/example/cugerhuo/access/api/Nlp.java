package com.example.cugerhuo.access.api;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.example.cugerhuo.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * nlp api
 * @author 施立豪 ,zhumeng
 */
public class Nlp {
    /**
     * 商品名获取接口
     */
    public static List<String> getNlpWords(String text1, Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetWord);
        String text=context.getString(R.string.Text);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+text+"="+text1;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<String> result=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            result= JSONArray.parseArray(response.body().string(),String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 商品分类获取接口
     */
    public static List<String> getNlpCatrgory(String text1, Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetClass);
        String text=context.getString(R.string.Text);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+text+"="+text1;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<String> result=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            result= JSONArray.parseArray(response.body().string(),String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }
}
