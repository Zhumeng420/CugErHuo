package com.example.cugerhuo.access.commodity;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.ConcernCommodity;
import com.example.cugerhuo.access.GetFocusRequest;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.tools.TracingHelper;
import com.example.cugerhuo.tools.entity.RespBean;
import com.example.cugerhuo.tools.entity.productParam;

import org.json.JSONException;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import okhttp3.FormBody;
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
     * 从redis获取商品数据
     * @param connection
     * @param id
     * @param context
     * @return
     */
    public static Commodity getCommodityFromRedis(RedisCommands<String, String> connection, int id, Context context)
    {
        /**
         * 查询redis，如果存在则返回，不存在查询mysql放入redis
         */
        String info=connection.hget("Commodity",String.valueOf(id));
        Commodity user = (Commodity) JSON.parseObject(info,Commodity.class);
        Log.i(TAG,"查询redis for commodity");
        /**
         * 没查到，从mysql查询，并插入redis
         */
        if(user==null)
        {
            user=getCommodity(id,context);
            String userString=JSON.toJSONString(user);
            connection.hset("Commodity",String.valueOf(id),userString);
            Log.i(TAG,"查询mysql for Commodity");
        }

        return user;
    }

    /**
     * 获取关注用户发布的商品
     * @param connection  redis连接
     * @param users 用户id列表
     * @param page  页数
     * @param context
     * @return
     * @throws JSONException
     */
    public static List<Commodity> getUsersCommodity(RedisCommands<String, String> connection,int []users,int page,Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetUsersCommodity);
        GetFocusRequest a=new GetFocusRequest();
        a.setPage(page);
        a.setUsers(users);
        List<Commodity> result=new ArrayList<>();
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(a), MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;

        try {
            response = okHttpClient.newCall(request).execute();
            result= (List<Commodity>) JSON.parseArray(response.body().string(), Commodity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }

    /**
     * 获取某用户的发布商品
     * @param users 用户id
     * @param context
     * @return
     * @throws JSONException
     */
    public static List<Commodity> getUsersCommodity(int users,Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetMyp);
        String userid=context.getString(R.string.userid);
        List<Commodity> result=new ArrayList<>();
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+userid+"="+users;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;

        try {
            response = okHttpClient.newCall(request).execute();
            result = (List<Commodity>) JSON.parseArray(response.body().string(),Commodity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 获取商品页推荐
     */
    public static  AbstractMap.SimpleEntry<List<Commodity>,List<PartUserInfo>> getRecommendComs(RedisCommands<String, String> con, int goodId, Context context)
    {
        Tracer tracer = GlobalTracer.get();
        OkHttpClient okHttpClient = new OkHttpClient();
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.GoodRecommend);
        String page=context.getString(R.string.page1);
        String goodid=context.getString(R.string.ProductId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+page+"&"+goodid+"="+goodId;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<Integer> recommedCom=new ArrayList<>();
        int result
                =-1;
        // 创建spann
        Span span = tracer.buildSpan("获取首页推荐流程").withTag("SetCommodityInfo ：", "setInfo").start();
        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
            try {
                response = okHttpClient.newCall(request).execute();
                JSONObject pa= JSONObject.parseObject(response.body().string());
                System.out.println(result);
                System.out.println("asdsa");
                JSONArray a=JSONArray.parseArray(pa.getString("object"));
                for(Object i:a)
                {
                    System.out.println("doashda"+i);
                    recommedCom.add((Integer) i);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            TracingHelper.onError(e, span);
            throw e;
        } finally {
            span.finish();
        } // 创建spann
        Span span1 = tracer.buildSpan("获取推荐商品和用户流程").withTag("SetCommodityInfo ：", "setInfo").start();
        try (Scope ignored = tracer.scopeManager().activate(span1,true)) {
            List<Commodity> tt = new ArrayList<>();
            List<PartUserInfo> mm = new ArrayList<>();
            long stime = System.currentTimeMillis();
            for (Integer i : recommedCom) {
                Commodity temp = CommodityOperate.getCommodityFromRedis(con, i, context);
                int id = temp.getUserId();
                System.out.println("userid" + id);
                tt.add(temp);
                mm.add(UserInfoOperate.getInfoFromRedis(con, id, context));
            }
            return new AbstractMap.SimpleEntry<>(tt, mm);
        }  catch (Exception e) {
        TracingHelper.onError(e, span1);
        throw e;
    } finally {
        span1.finish();
    }


        }
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

    /**
     * 模糊搜索商品接口
     * @param search 搜索的内容
     * @param context 获取映射文件
     * @return 返回商品实体类
     * @author 唐小莉
     * @time 2023/5/4
     */
    public static List<Commodity> getSearchCommodity(String search,Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        List<Commodity> commodities=new ArrayList<>();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.searchCommodity);
        String cCommodity=context.getString(R.string.commodity);
        /**
         * 发送请求
         */
       // String url="http://"+ip+"/"+router+"?"+cCommodity+"="+search;
        String url="http://"+ip+"/"+router;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(cCommodity, String.valueOf(search));
        /**
         * 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            commodities=JSONArray.parseArray(response.body().string(),Commodity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
       List<Commodity> res=new ArrayList<>();
        for(Commodity commodity:commodities)
        {
            if(commodity!=null){res.add(commodity);}
        }
        return res;
    }

    /**
     * 获取关注的人发布的商品
     * @param concernCommodity 关注的人列表
     * @param context 获取映射文件
     * @return 返回商品列表
     * @author 唐小莉
     * @time 2023/5/5
     */
    public static List<Commodity> getConcernCommodity(ConcernCommodity concernCommodity,Context context){

        OkHttpClient okHttpClient = new OkHttpClient();
        List<Commodity> commodities=new ArrayList<>();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.commodityByConcern);
        String getFocusRequest=context.getString(R.string.getFocusRequest);
        /**
         * 发送请求
         */
        // String url="http://"+ip+"/"+router+"?"+cCommodity+"="+search;
        String url="http://"+ip+"/"+router;
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(concernCommodity), MediaType.parse("application/json")
        );
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;

        try {
            response = okHttpClient.newCall(request).execute();
            commodities=JSONArray.parseArray(response.body().string(),Commodity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commodities;
    }


    public static void remove(RedisCommands<String, String> connection, int id, Context context)
    {
        connection.hdel("Commodity",String.valueOf(id));
    }


    /**
     * 获取实时推荐
     */
    public static  AbstractMap.SimpleEntry<List<Commodity>,List<PartUserInfo>> getOnlineRecommendComs(RedisCommands<String, String> con, int goodId,int page2, Context context)
    {
        Tracer tracer = GlobalTracer.get();
        OkHttpClient okHttpClient = new OkHttpClient();
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.OnlineRecommend);
        String page=context.getString(R.string.page);
        String goodid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+page+"="+page2+"&"+goodid+"="+goodId;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<Integer> recommedCom=new ArrayList<>();
        int result
                =-1;
        // 创建spann
        Span span = tracer.buildSpan("获取首页推荐流程").withTag("SetCommodityInfo ：", "setInfo").start();
        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
            try {
                response = okHttpClient.newCall(request).execute();
                JSONObject pa= JSONObject.parseObject(response.body().string());
                System.out.println(result);
                System.out.println("asdsa");
                JSONArray a=JSONArray.parseArray(pa.getString("object"));
                for(Object i:a)
                {
                    System.out.println("doashda"+i);
                    recommedCom.add((Integer) i);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            TracingHelper.onError(e, span);
            throw e;
        } finally {
            span.finish();
        } // 创建spann
        Span span1 = tracer.buildSpan("获取推荐商品和用户流程").withTag("SetCommodityInfo ：", "setInfo").start();
        try (Scope ignored = tracer.scopeManager().activate(span1,true)) {
            List<Commodity> tt = new ArrayList<>();
            List<PartUserInfo> mm = new ArrayList<>();
            long stime = System.currentTimeMillis();
            for (Integer i : recommedCom) {
                Commodity temp = CommodityOperate.getCommodityFromRedis(con, i, context);
                int id = temp.getUserId();
                System.out.println("userid" + id);
                tt.add(temp);
                mm.add(UserInfoOperate.getInfoFromRedis(con, id, context));
            }
            return new AbstractMap.SimpleEntry<>(tt, mm);
        }  catch (Exception e) {
            TracingHelper.onError(e, span1);
            throw e;
        } finally {
            span1.finish();
        }

    }
}
