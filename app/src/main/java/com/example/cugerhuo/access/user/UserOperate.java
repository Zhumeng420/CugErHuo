package com.example.cugerhuo.access.user;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.example.cugerhuo.R;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public static boolean insertPhoneBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertPhoneToBloom);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
         String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端的插入手机号到redis布隆过滤器接口
     * @param qqOpenId 手机号
     * @param context 用于获取反射常量
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean insertQqBloom(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertQqToBloom);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过手机号向mysql插入数据的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean insertByPhone(String phone, String username1, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */

        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertUserByPhone);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        String username=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone+"&"+username+"="+username1;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过qq号向mysql插入数据的接口
     * @param qqOpenId 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean insertByQq(String qqOpenId, String username, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */

        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertUserByQq);
        String qq=context.getString(R.string.Qq);
        String userName=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId+"&"+userName+"="+username;
         //构建Request，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }
    /**
     * 调用服务端通过手机号查询redis是否存在的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean isPhoneExistBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsPhoneExistInBloom);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过qq号查询redis是否存在的接口
     * @param qqOpenId qq号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean isQqExistBloom(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsQqExistInBloom);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }
    /**
     * 调用服务端通过手机号查询redis，手机号是否被封的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean isPhoneBanedBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsPhoneBanedInBloom);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }
    /**
     * 调用服务端通过qq号查询redis，qq号是否被封的接口
     * @param qqOpenId 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean isQqBanedBloom(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsQqBanedInBloom);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过手机号查询用户id
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/26
     */
    public static int getId(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetIdByPhone);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        int result =-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 调用服务端通过qq号查询用户id
     * @param qqOpenId qq号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static int getQqId(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetIdByQq);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
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
    public static boolean insertUserToTu(String username, int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.InsertUserToTu);
        String name=context.getString(R.string.Name);
        String uid=context.getString(R.string.UserId);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
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
    public static int getFocusNum(int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.FocusNum);
        String uid=context.getString(R.string.UserId);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
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
    public static int getFansNum(int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.FansNum);
        String uid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
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
     * 获取关注列表用户id
     * @param id 登录用户id
     * @param context 获取映射文件
     * @return 返回关注列表id
     * @author 唐小莉
     * @time 2023/4/11 19:15
     */
    public static List<Integer> getConcernId(int id, Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.Focus);
        String uid=context.getString(R.string.UserId);

        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        List<Integer> result=new ArrayList<>();
        //System.out.println("哇哇哇哇哇哇哇+"+id);
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            result= JSONArray.parseArray(response.body().string(),Integer.class);
            //System.out.println("result  aaaaaaa"+result.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }

}
