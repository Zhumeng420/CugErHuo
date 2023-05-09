package com.example.cugerhuo.access.comment;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Comment;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.tools.entity.RespBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 收藏接口调用
 * @Author: 李柏睿
 * @Time: 2023/5/9 21:26
 */
public class CollectOperate {

    /**
     * 收藏商品
     * @Author: 李柏睿
     * @Time: 2023/5/9 21:36
     */
    public static boolean insertCollect(int userId, int productId, Context context) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.collectProduct);
        String uid=context.getString(R.string.UserId);
        String pid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        /**循环form表单，将表单内容添加到form builder中*/
        /**构建formBody，将其传入Request请求中*/
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(userId));
        builder.add(pid, String.valueOf(productId));

        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        boolean result =false;
        try {
            response = okHttpClient.newCall(request).execute();
            RespBean a= (RespBean) JSON.parseObject(response.body().string(), RespBean.class);
            result= 200==a.getCode();
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }

    /**
     * 取消收藏
     * @Author: 李柏睿
     * @Time: 2023/5/9 23:03
     */
    public static boolean deleteCollect(int userId, int productId, Context context) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.deleteCollect);
        String uid=context.getString(R.string.UserId);
        String pid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        /**循环form表单，将表单内容添加到form builder中*/
        /**构建formBody，将其传入Request请求中*/
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(userId));
        builder.add(pid, String.valueOf(productId));

        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).delete(body).build();
        Response response = null;
        boolean result =false;
        try {
            response = okHttpClient.newCall(request).execute();
            RespBean a= (RespBean) JSON.parseObject(response.body().string(), RespBean.class);
            result= 200==a.getCode();
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }

    /**
     * 判断是否收藏
     * @Author: 李柏睿
     * @Time: 2023/5/9 23:03
     */
    public static boolean isCollect(int userId, int productId, Context context) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.checkCollect);
        String uid=context.getString(R.string.UserId);
        String pid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+uid+"="+userId+"&"+pid+"="+productId;
        /**循环form表单，将表单内容添加到form builder中*/
        /**构建formBody，将其传入Request请求中*/
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(userId));
        builder.add(pid, String.valueOf(productId));

        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean result =false;
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            JSONObject obj=new JSONObject(response.body().string());
            if("true".equals(obj.getString("object"))){
                result=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }


    /**
     * 获取收藏总数
     * @Author: 李柏睿
     * @Time: 2023/5/9 23:03
     */
    public static int collectNum(int productId, Context context) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.collectNum);
        String uid=context.getString(R.string.UserId);
        String pid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+pid+"="+productId;
        /**循环form表单，将表单内容添加到form builder中*/
        /**构建formBody，将其传入Request请求中*/
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(pid, String.valueOf(productId));

        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        int result =0;
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            JSONObject obj=new JSONObject(response.body().string());
            if("200".equals(obj.getString("code"))){
                result= Integer.parseInt(obj.getString("object"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }


    /**
     * 获取我的收藏
     * @Author: 李柏睿
     * @Time: 2023/5/9 23:03
     */
    public static List<Integer> myCollect(int userId, Context context) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.myCollect);
        String uid=context.getString(R.string.UserId);
        String pid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String result = "hi";
        List<Integer> myCollects=new ArrayList<>();
        int pageId=0;
        while (result!=null){
            String url="http://"+ip+"/"+router+"?"+uid+"="+userId+"&"+"page"+"="+pageId;
            Request request = new Request.Builder().url(url).get().build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                response.body();
                JSONObject obj=new JSONObject(response.body().string());
                if("200".equals(obj.getString("code"))){
                    String collects = obj.getString("object");
                    collects=collects.substring(1,collects.length()-1);
                    if("".equals(collects)){
                        result=null;
                        return myCollects;
                    }
                    String []coll=collects.split(",");
                    int length=coll.length;
                    for(int i = 0;i<length;i++){
                        myCollects.add(Integer.valueOf(coll[i]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pageId++;
        }
        return myCollects;

    }


    /**
     * 获取我的收藏数量
     * @Author: 李柏睿
     * @Time: 2023/5/9 23:03
     */
    public static int myCollectNum(int userId, Context context) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.myCollect);
        String uid=context.getString(R.string.UserId);
        String pid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String result = "hi";
        List<Integer> myCollects=new ArrayList<>();
        int pageId=0;
        while (result!=null){
            String url="http://"+ip+"/"+router+"?"+uid+"="+userId+"&"+"page"+"="+pageId;
            Request request = new Request.Builder().url(url).get().build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                response.body();
                JSONObject obj=new JSONObject(response.body().string());
                if("200".equals(obj.getString("code"))){
                    String collects = obj.getString("object");
                    collects=collects.substring(1,collects.length()-1);
                    if("".equals(collects)){
                        result=null;
                        return myCollects.toArray().length;
                    }
                    String []coll=collects.split(",");
                    int length=coll.length;
                    for(int i = 0;i<length;i++){
                        myCollects.add(Integer.valueOf(coll[i]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pageId++;
        }
        return myCollects.toArray().length;

    }
}
