package com.example.cugerhuo.access.commerce;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.entity.RespBean;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommerceOperate {
    /**
     * 插入交易订单
     * @param commerce  交易信息
     * @param context
     * @return
     * @throws JSONException
     */
    public static boolean insertCommerce(Commerce commerce, Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertCommerce);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(commerce), MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        boolean result
                =false;
        try {
            response = okHttpClient.newCall(request).execute();
            RespBean a= (RespBean) JSON.parseObject(response.body().string(), RespBean.class);
            result= (a.getCode()==200);
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 更新订单状态
     * @param id  订单id
     * @param state1  状态 0关闭，
     *      * 0关闭，1，等待交易，2完成
     *
     * @param context
     * @return  是否成功
     */
    public static boolean setState(int id,int state1, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.SetUserImage);
        String state=context.getString(R.string.State);
        String commerceId=context.getString(R.string.CommerceId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+state+"="+state1+"&"+commerceId+"="+id;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isSeted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            org.json.JSONObject obj=new org.json.JSONObject(response.body().string());
            isSeted="true".equals(obj.getString("object"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSeted;
    }


}
