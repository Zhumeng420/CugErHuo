package com.example.cugerhuo.access.comment;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Comment;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.tools.entity.RespBean;

import org.json.JSONException;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 评论接口调用
 * @author 施立豪
 * @time 2023-5-4
 */
public class CommentOperate {
    /**
     * 插入评论
     * @param comment 评论
     * @param context
     * @return
     * @throws JSONException
     */
    public static boolean insertComment(Comment comment, Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertComment);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(comment), MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        boolean result
                =false;
        try {
            response = okHttpClient.newCall(request).execute();
            RespBean a= (RespBean) JSON.parseObject(response.body().string(), RespBean.class);
            result= 200==a.getCode();
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 获取评论
     * @param con
     * @param id 商品id
     * @param context
     * @return
     */
    public static  AbstractMap.SimpleEntry<List<Comment>,List<PartUserInfo>> getRewards(RedisCommands<String, String> con, int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.CommentGet);
        String page1=context.getString(R.string.CommodityId);


        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+page1+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<Comment> rewards=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
        RespBean a=    JSON.parseObject(response.body().string(),RespBean.class);
            if(a.getObject()!=null){
            rewards=JSON.parseArray(a.getObject().toString(),Comment.class);}
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<PartUserInfo> mm = new ArrayList<>();
        for (Comment i : rewards) {
            mm.add(UserInfoOperate.getInfoFromRedis(con, i.getUserid(), context));
        }
        return new AbstractMap.SimpleEntry<>(rewards, mm);

    }
}
