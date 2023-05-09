package com.example.cugerhuo.access.evaluate;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;

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

    public static List<Commodity> getEvaluate(int id, Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        List<Commodity> commodities=new ArrayList<>();
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
            JSONObject pa= JSONObject.parseObject(response.body().string());
            commodities= JSONArray.parseArray(pa.getString("commodity"),Commodity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commodities;
    }
}
