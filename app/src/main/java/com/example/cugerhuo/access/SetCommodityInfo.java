package com.example.cugerhuo.access;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.util.MsgEvent1;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.tools.TracingHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.lettuce.core.api.sync.RedisCommands;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetCommodityInfo {
    public static boolean setInfo(int id, Context context)
    {
        Tracer tracer = GlobalTracer.get();

        OkHttpClient okHttpClient = new OkHttpClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long stime2 = System.currentTimeMillis();
                List<Integer> recommedCom=new ArrayList<>();
                /**
                 * 建立连接对象
                 */
                LettuceBaseCase lettuce=new LettuceBaseCase();
                /**
                 * 获取连接
                 */
                RedisCommands<String, String> con=lettuce.getSyncConnection();
                /**
                 * 获取XML文本
                 */
                String ip=context.getString(R.string.Tuip);
                String router=context.getString(R.string.recommendCom);
                String page=context.getString(R.string.page1);
                String uid=context.getString(R.string.UserId);
                /**
                 * 发送请求
                 */
                String url="http://"+ip+"/"+router+"?"+page+"&"+uid+"="+id;
                Request request = new Request.Builder().url(url).get().build();
                Response response = null;
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
            }
                // 创建spann
                Span span1 = tracer.buildSpan("获取推荐商品和用户流程").withTag("SetCommodityInfo ：", "setInfo").start();
                try (Scope ignored = tracer.scopeManager().activate(span1,true)) {
                long stime3 = System.currentTimeMillis();
                System.out.println("time0"+(stime3-stime2));

                List<Commodity>  tt=new ArrayList<>();
                List<PartUserInfo> mm=new ArrayList<>();
                long stime = System.currentTimeMillis();
                for(Integer i:recommedCom)
                {
                    Commodity temp= CommodityOperate.getCommodityFromRedis(con,i,context);
                    int id=temp.getUserId();
                    System.out.println("userid"+id);
                    tt.add(temp);
                    PartUserInfo tempUser=UserInfoOperate.getInfoFromRedis(con,id,context);
                    mm.add(tempUser);
                }

                long stime1 = System.currentTimeMillis();
                System.out.println("time1"+(stime1-stime));
                RecommendInfo.setCommodityList(tt);
                RecommendInfo.setPartUserInfoList(mm);

                // 构建广播Intent
                System.out.println(stime1);
                } catch (Exception e) {
                    TracingHelper.onError(e, span1);
                    throw e;
                } finally {
                    span1.finish();
                }
                // 发送广播
                EventBus.getDefault().post(new MsgEvent1("子线程发的消息1"));
            }
        }).start();

return true;
    }

    /**
     * 首页刷新推荐
     * @param id 用户id
     * @param page2 页数
     * @param context 获取映射文件
     * @return 返回是否成功
     * @author 唐小莉
     * @time 2023/5/2
     */
    public static boolean setInfoRefresh(int id, int page2,Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Integer> recommedCom=new ArrayList<>();
                /**
                 * 建立连接对象
                 */
                LettuceBaseCase lettuce=new LettuceBaseCase();
                /**
                 * 获取连接
                 */
                RedisCommands<String, String> con=lettuce.getSyncConnection();
//                /**
//                 * 获取XML文本
//                 */
//                String ip=context.getString(R.string.Tuip);
//                String router=context.getString(R.string.recommendCom);
//                String page=context.getString(R.string.page);
//                String uid=context.getString(R.string.UserId);
//                /**
//                 * 发送请求
//                 */
//                String url="http://"+ip+"/"+router+"?"+page+"="+page2+"&"+uid+"="+id;
//                Request request = new Request.Builder().url(url).get().build();
//                Response response = null;
//                int result
//                        =-1;
//                try {
//                    response = okHttpClient.newCall(request).execute();
//                    JSONObject pa= JSONObject.parseObject(response.body().string());
//                    System.out.println(result);
//                    System.out.println("asdsa");
//                    JSONArray a=JSONArray.parseArray(pa.getString("object"));
//                    for(Object i:a)
//                    {
//                        System.out.println("doashda"+i);
//                        recommedCom.add((Integer) i);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                List<Commodity>  tt=new ArrayList<>();
//                List<PartUserInfo> mm=new ArrayList<>();
//                for(Integer i:recommedCom)
//                {
//                    Commodity temp= CommodityOperate.getCommodityFromRedis(con,i,context);
//                    int id=temp.getUserId();
//                    System.out.println("userid"+id);
//                    tt.add(temp);
//                    mm.add(UserInfoOperate.getInfoFromRedis(con,id,context));
//                }
//                RecommendInfo.setCommodityList(tt);
//                RecommendInfo.setPartUserInfoList(mm);
                /**
                 * 实时推荐
                 */
                Map.Entry<List<Commodity>, List<PartUserInfo>> result=CommodityOperate.getOnlineRecommendComs(con,id,0,context);
                RecommendInfo.setPartUserInfoList(result.getValue());
                RecommendInfo.setCommodityList(result.getKey());

                // 发送广播

                EventBus.getDefault().post(new MsgEvent1("子线程发的消息1"));
            }
        }).start();

        return true;
    }
}
