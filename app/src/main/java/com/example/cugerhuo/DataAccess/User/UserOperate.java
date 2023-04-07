package com.example.cugerhuo.DataAccess.User;

import android.content.Context;

import com.example.cugerhuo.R;

import java.io.IOException;

import okhttp3.FormBody;
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
     * 调用服务端的插入手机号到redis布隆过滤器接口
     * @param qq 手机号
     * @param context 用于获取反射常量
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean InsertQqBloom(String qq, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.InsertQqToBloom);
        String Qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+Qq+"="+qq;
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
    public static boolean InsertByPhone(String phone,String username,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */

        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.InsertUserByPhone);
        String PhoneNumber=context.getString(R.string.PhoneNumber);
        String Username=context.getString(R.string.Username);


        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+PhoneNumber+"="+phone+"&"+Username+"="+username;
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
     * 调用服务端通过qq号向mysql插入数据的接口
     * @param qq 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean InsertByQq(String qq,String username,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */

        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.InsertUserByQq);
        String Qq=context.getString(R.string.Qq);
        String Username=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+Qq+"="+qq+"&"+Username+"="+username;
         //构建Request，将其传入Request请求中
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
        //构建Request，将其传入Request请求中

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
     * 调用服务端通过qq号查询redis是否存在的接口
     * @param qq qq号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean IsQqExistBloom(String qq,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.IsQqExistInBloom);
        String Qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+Qq+"="+qq;
        //构建Request，将其传入Request请求中

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
        //构建Request，将其传入Request请求中

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
     * 调用服务端通过qq号查询redis，qq号是否被封的接口
     * @param qq 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean IsQqBanedBloom(String qq,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.IsQqBanedInBloom);
        String Qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+Qq+"="+qq;
        //构建Request，将其传入Request请求中

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
     * 调用服务端通过手机号查询用户id
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/26
     */
    public static int GetId(String phone,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.GetIdByPhone);
        String PhoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+PhoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        int result
                =-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 调用服务端通过qq号查询用户id
     * @param qq qq号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static int GetQqId(String qq,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.GetIdByQq);
        String Qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+Qq+"="+qq;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        int result
                =-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 插入id和用户名到图数据库
     * @param username 用户名
     * @param id 用户id
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/26
     */
    public static boolean InsertUserToTu(String username,int id,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.Tuip);
        String Router=context.getString(R.string.InsertUserToTu);
        String name=context.getString(R.string.Name);
        String uid=context.getString(R.string.UserId);

        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        builder.add(name,username);
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        int result
                =-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }return result==200;
    }

    /**
     * 获取用户关注数
     * @param id 用户id
     * @param context 获取映射文件
     * @return 数量
     * @author 施立豪
     * @time 2023/3/27
     */
    public static int  GetFocusNum(int id,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.Tuip);
        String Router=context.getString(R.string.FocusNum);
        String uid=context.getString(R.string.UserId);

        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        int result=-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 获取用户粉丝
     * @param id 用户id
     * @param context 获取映射文件
     * @return 数量
     * @author 施立豪
     * @time 2023/3/27
     */
    public static int  GetFansNum(int id,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.Tuip);
        String Router=context.getString(R.string.FansNum);
        String uid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        int result=-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

}
