package com.example.cugerhuo.DataAccess.User;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.cugerhuo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.lettuce.core.api.sync.RedisCommands;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * 用户资料接口调用类
 * @author 施立豪
 * @time 2023/4/9
 */
public class UserInfoOperate
{
    /**
     * 插入用户
     * @param id 用户id
     * @param userName 用户名
     * @param context   获取映射文件
     * @return  是否成功
     */
    public static boolean insertUser(int id, String userName, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.InsertUserInfo);
        String UserName=context.getString(R.string.Username);
        String UserID=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+UserName+"="+userName+"&"+UserID+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean IsSeted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            IsSeted=obj.getString("object").equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return IsSeted;
    }
    /**
     * 调用服务端向mysql用户资料表设置头像url
     * @param id 用户id
     * @param imageUrl 图像在桶中的路径
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/4/9
     */
    public static boolean setImage(int id, String imageUrl, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.SetUserImage);
        String ImageUrl=context.getString(R.string.ImageUrl);
        String UserID=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+ImageUrl+"="+imageUrl+"&"+UserID+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean IsSeted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            IsSeted=obj.getString("object").equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return IsSeted;
    }

    /**
     * 调用通过id获取头像接口
     * @param id   用户id
     * @param context 用于文件映射
     * @return      图片url
     */
    public static String getImage(int id, Context context)
    {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String Ip=context.getString(R.string.ip);
        String Router=context.getString(R.string.GetUserImage);
        String UserID=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+Ip+"/"+Router+"?"+UserID+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        String result="";
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            result=obj.getString("object");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 取用户名，头像，个签，id从mysql
     * @param id   用户id
     * @param context 用于文件映射
     * @return      图片url
     */
    public static PartUserInfo getInfoFromMysql(int id, Context context)
    {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetPartUserInfo);
        String userID=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+userID+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        PartUserInfo user=null;
        try {
            response = okHttpClient.newCall(request).execute();
            user = (PartUserInfo) JSON.parseObject(response.body().string(),PartUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }


    /**
     * 获取用户名，头像，个签，id
     * @param connection        redis连接
     * @param id            用户id
     * @param context       获取xml文本
     * @return    用户部分信息实体类
     */
    public static PartUserInfo getInfoFromRedis(RedisCommands<String, String> connection, int id, Context context)
    {
        /**
         * 查询redis，如果存在则返回，不存在查询mysql放入redis
         */
        String info=connection.hget("UserInfo",String.valueOf(id));
        PartUserInfo user = (PartUserInfo) JSON.parseObject(info,PartUserInfo.class);
        Log.i(TAG,"查询redis for userinfo");
        /**
         * 没查到，从mysql查询，并插入redis
         */
        if(user==null)
        {
            user=getInfoFromMysql(id,context);
            String userString=JSON.toJSONString(user);
            connection.hset("UserInfo",String.valueOf(id),userString);
            Log.i(TAG,"查询mysql for userinfo");
        }
        return user;
    }
}
