package com.example.cugerhuo.access.reward;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Reward;
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
 * 悬赏操作类
 * @author 施立豪
 * @time 2023-5-4
 */
public class RewardOperate {
    /**
     * 插入mysql悬赏
     * @param reward 悬赏实体
     * @param context
     * @return
     * @throws JSONException
     */
    public static int insertReward(Reward reward, Context context) throws JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.RewardInsert);
        /**
         * 发送请求
         */
        String url="http://"+ip+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        RequestBody body = RequestBody.create(
                JSONObject.toJSONString(reward), MediaType.parse("application/json")
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
     * 获取悬赏
     * @param page 第几页
     * @param num  每页个数
     * @param context
     */

    public static  AbstractMap.SimpleEntry<List<Reward>,List<PartUserInfo>> getRewards(RedisCommands<String, String> con, int page,int num, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.RewardGet);
        String page1=context.getString(R.string.page);
        String num1=context.getString(R.string.num);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+page1+"="+page+"&"+num1+"="+num;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<Reward> rewards=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            rewards=JSON.parseArray(response.body().string(),Reward.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
            List<PartUserInfo> mm = new ArrayList<>();
            for (Reward i : rewards) {
                mm.add(UserInfoOperate.getInfoFromRedis(con, i.getUserid(), context));
            }
            return new AbstractMap.SimpleEntry<>(rewards, mm);

    }


}
