package com.example.cugerhuo.DataAccess.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于获取访问对象存储的key，secret，token
 * @author 施立豪
 * @time 2023/4/7
 */
public class OssSecret {
    /**
     * 调用服务端获取临时访问对象存储的keyid接口
     * @return list中包含三项，第一项  accesskeyid，第二项secret，第三项token
     * @author 施立豪
     * @time 2023/4/7
     */
    public static List<String> GetOss()
    {
        List<String> OssMessage=new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 发送请求
         */
        String url="http://123.249.120.9:8082/secret/oos";
        //构建Request，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            JSONObject obj=new JSONObject(response.body().string());
            System.out.println("response.accessId"+obj.getString("accessId"));
            OssMessage.add(obj.getString("accessId"));
            OssMessage.add(obj.getString("accessSecret"));
            OssMessage.add(obj.getString("securityToken"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return OssMessage;
    }
}
