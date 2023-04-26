package com.example.cugerhuo.access.user;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.cugerhuo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
     * @param userName1 用户名
     * @param context   获取映射文件
     * @return  是否成功
     */
    public static boolean insertUser(int id, String userName1, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertUserInfo);
        String userName=context.getString(R.string.Username);
        String userid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+userName+"="+userName1+"&"+userid+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isSeted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            isSeted="true".equals(obj.getString("object"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSeted;
    }
    /**
     * 调用服务端向mysql用户资料表设置头像url
     * @param id 用户id
     * @param imageUrl1 图像在桶中的路径
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/4/9
     */
    public static boolean setImage(int id, String imageUrl1, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.SetUserImage);
        String imageUrl=context.getString(R.string.ImageUrl);
        String userid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+imageUrl+"="+imageUrl1+"&"+userid+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isSeted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            isSeted="true".equals(obj.getString("object"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSeted;
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
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetUserImage);
        String userid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+userid+"="+id;
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
        String userid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+userid+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        PartUserInfo user=new PartUserInfo();
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
        if(user!=null)
        {
            user.setUserId(id);
        }
        return user;
    }
    public static void remove(RedisCommands<String, String> connection, int id, Context context)
    {
        connection.hdel("UserInfo",String.valueOf(id));
    }

    /**
     * 更改用户昵称
     * @param id 用户id
     * @param newName 用户新昵称
     * @param context 获取映射文件
     * @return 返回是否更新成功
     * @author 唐小莉
     * @time 2023/4/26
     */
    public static boolean updateUserName(int id,String newName,Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.updateName);
        String userid=context.getString(R.string.UserId);
        String username=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+userid+"="+id+"&"+username+"="+newName;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isUpdate=false;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            if("true".equals(obj.getString("object"))){
                isUpdate=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isUpdate;
    }

    /**
     * 更改用户签名
     * @param id 用户id
     * @param sign 用户新签名
     * @param context 获取映射文件
     * @return 返回是否更新成功
     * @author 唐小莉
     * @time 2023/4/26
     */
    public static boolean updateUserSign(int id,String sign,Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.updateSign);
        String userid=context.getString(R.string.UserId);
        String signature=context.getString(R.string.Signature);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+signature+"="+sign+"&"+userid+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isUpdate=false;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            if("true".equals(obj.getString("object"))){
                isUpdate=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isUpdate;
    }


}
