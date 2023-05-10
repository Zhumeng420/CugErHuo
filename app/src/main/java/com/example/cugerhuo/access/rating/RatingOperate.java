package com.example.cugerhuo.access.rating;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.entity.RespBean;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 推荐评分接口
 * @author 施立豪
 * @time 2023/5/10
 */
public class RatingOperate {
    /**
     * 插入评分接口
     * @param ratingParam
     * @param context
     * @return
     */
    public static boolean insertRatingToRecommend(RatingParam ratingParam,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.RateRecommend);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;

        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(ratingParam), MediaType.parse("application/json"));
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
}
