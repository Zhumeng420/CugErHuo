package com.example.cugerhuo.access.evaluate;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.entity.RespBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 商品评价接口
 * @author 唐小莉
 * @time 2023/5/9
 */
public class CommodityEvaluateOperate {

    public static List<EvaluationInfo> getEvaluate(int id, Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        List<EvaluationInfo> evaluationInfos=new ArrayList<>();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.getEvaluation);
        String userid=context.getString(R.string.userid);
        /**
         * 发送请求
         */
        // String url="http://"+ip+"/"+router+"?"+cCommodity+"="+search;

        String url="http://"+ip+"/"+router+"?"+userid+"="+id;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;

        try {
            response = okHttpClient.newCall(request).execute();
            evaluationInfos= JSONArray.parseArray(response.body().string(),EvaluationInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return evaluationInfos;
    }

    /**
     * 插入评价
     * @param evlaution
     * @param context
     * @return   是否成功
     */
    public  static boolean insertEmptyEvlution(Evaluation evlaution,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertEVALUATION);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(evlaution), MediaType.parse("application/json")
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
     * 更新评价  写评价时调用
     * @param evlaution 评价实体
     * @param context
     * @return 是否成功
     */
    public  static boolean updateEvlution(Evaluation evlaution,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.UpdateEVALUATION);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(evlaution), MediaType.parse("application/json")
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
}
