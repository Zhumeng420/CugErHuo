package com.example.cugerhuo.access;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.util.MsgEvent1;
import com.example.cugerhuo.tools.LettuceBaseCase;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetCommodityInfo {
    public static boolean setInfo(int id, Context context)
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
                List<Commodity>  tt=new ArrayList<>();
                List<PartUserInfo> mm=new ArrayList<>();
                for(Integer i:recommedCom)
                {
                    Commodity temp= CommodityOperate.getCommodityFromRedis(con,i,context);
                    int id=temp.getUserId();
                    tt.add(temp);
                    mm.add(UserInfoOperate.getInfoFromRedis(con,id,context));
                }
                RecommendInfo.setCommodityList(tt);
                RecommendInfo.setPartUserInfoList(mm);
                // 构建广播Intent
                Intent intent = new Intent("update");
                intent.setAction("com.example.cugerhuo.fragment.SuggestFragment.BroadcastReceiver");
                // 发送广播
                EventBus.getDefault().post(new MsgEvent1("子线程发的消息1"));
            }
        }).start();

return true;
    }
}
