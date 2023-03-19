package com.example.cugerhuo.DataAccess.User;

import android.content.Context;

import com.example.cugerhuo.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于调用用户类的接口
 * @author 施立豪
 * @time 2023/3/18
 */
public class UserOperate {
    /**
     * 调用服务端的插入手机号到redis布隆过滤器接口
     * @param phone 手机号
     * @param context 用于获取反射常量
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean InsertPhoneBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.InsertPhoneToBloom);
        String PhoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
         String url="http://"+Ip+"/"+Router+"?"+PhoneNumber+"="+phone;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean IsInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            IsInserted=response.body().string().equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }return IsInserted;
    }

    /**
     * 调用服务端通过手机号向mysql插入数据的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean InsertByPhone(String phone,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.InsertUserByPhone);
        String PhoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+PhoneNumber+"="+phone;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean IsInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            IsInserted=response.body().string().equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }return IsInserted;
    }
    /**
     * 调用服务端通过手机号查询redis是否存在的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean IsPhoneExistBloom(String phone,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.IsPhoneExistInBloom);
        String PhoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+PhoneNumber+"="+phone;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean IsInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            IsInserted=response.body().string().equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }return IsInserted;
    }
    /**
     * 调用服务端通过手机号查询redis，手机号是否被封的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean IsPhoneBanedBloom(String phone,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.IsPhoneBanedInBloom);
        String PhoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+PhoneNumber+"="+phone;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean IsInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            IsInserted=response.body().string().equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }return IsInserted;
    }

}
