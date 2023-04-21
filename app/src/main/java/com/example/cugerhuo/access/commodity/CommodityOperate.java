package com.example.cugerhuo.access.commodity;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.tools.entity.RespBean;
import com.example.cugerhuo.tools.entity.productParam;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 商品mysql接口操作类
 * @author 施立豪
 * @time 2023/4/21
 */
public class CommodityOperate {

    /**
     * 获取商品信息
     * @param id
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/4/21
     */
    public static Commodity getCommodity(int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Commodity commodity=null;
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetCommodity);
        String aid=context.getString(R.string.ID);
            /**
             * 发送请求
             */
            String url="http://"+ip+"/"+router+"?"+aid+"="+id;
            Request request = new Request.Builder().url(url).get().build();
            Response response = null;
            String result="";
            try {
                response = okHttpClient.newCall(request).execute();
                commodity = (Commodity) JSON.parseObject(response.body().string(),Commodity.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return commodity;
        }
    /**
     * 插入商品到数据库
     * @param commodity 商品
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/4/21
     */
    public static int insertCommodity(Commodity commodity, Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertCommodity);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(commodity), MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        int result
                =-1;
        try {
            response = okHttpClient.newCall(request).execute();
           RespBean a= (RespBean) JSON.parseObject(response.body().string(), RespBean.class);
            result= (int) a.getObject();
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 插入发布关系到图数据库
     * @param userid 用户名
     * @param id 商品id
     * @param name 商品名
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/4/21
     */
    public static boolean insertUserToTu(int userid, int id, String name,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.InsertCommodityTu);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;

        productParam product=new productParam(name,id,userid);
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(product), MediaType.parse("application/json")
        );
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
}
