package com.example.cugerhuo.DataAccess.User;

import android.content.Context;

import com.example.cugerhuo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
     * 调用服务端向mysql用户资料表设置头像url
     * @param id 用户id
     * @param imageUrl 图像在桶中的路径
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/4/9
     */
    public static boolean SetImage(int id, String imageUrl, Context context)
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
    public static String GetImage(int id,Context context)
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
}
