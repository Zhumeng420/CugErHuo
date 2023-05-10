package com.example.cugerhuo.graph;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cugerhuo.graph.util.Base64Util;
import com.example.cugerhuo.graph.util.FileUtil;
import com.example.cugerhuo.graph.util.HttpUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 图搜索 入库与搜索操作
 * @author 施立豪
 * @time 2023/4/29
 */
public class GraphOperate {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    public static String graphToken;
    public static String getToken() throws IOException{
        if(graphToken==null){
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=0DHQGVPEGRQz8KUPpXfFACk3&client_secret=P5cq4MP3r8YpTrD8rDqpPRBZVvhfNbYV&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        JSONObject pa= JSONObject.parseObject(response.body().string());
        graphToken=pa.getString("access_token");
        //可以使用parseObject(params，Class<T> clazz)直接转换成需要的Bean
       }
            return graphToken;
    }

    /**
     * 商品图片加入图像检索库类别1
     * @param path   路径列表
     * @param productId  商品id
     * @return  null
     */
    public static Boolean productAdd(List<String> path, int productId) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/product/add";
        boolean res=false;
        try {
            for(String filePath:path){
            // 本地文件路径
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam+"&class_id1=1&brief="+String.valueOf(productId);
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getToken();
            String result = HttpUtil.post(url, accessToken, param);
            JSONObject ps=JSONObject.parseObject(result);

            System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 商品图片加入图像检索库类别2
     * @param path   路径列表
     * @param productId  商品id
     * @return  null
     */
    public static Boolean productAdd1(List<String> path, int productId) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/product/add";
        boolean res=false;
        try {
            for(String filePath:path){
                // 本地文件路径
                byte[] imgData = FileUtil.readFileByBytes(filePath);
                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                String param = "image=" + imgParam+"&class_id1=2&brief="+String.valueOf(productId);
                // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                String accessToken = getToken();
                String result = HttpUtil.post(url, accessToken, param);
                JSONObject ps=JSONObject.parseObject(result);

                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Integer> productSearch(String path) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/product/search";
        List<Integer> res=new ArrayList<>();
        try {

            // 本地文件路径
            String filePath = path;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getToken();

            String result = HttpUtil.post(url, accessToken, param);
            JSONObject pa= JSONObject.parseObject(result);
            System.out.println(result);
            System.out.println("asdsa");
            for(Object i:JSONArray.parseArray(pa.getString("result")))
            {
                System.out.println(i.toString());
                JSONObject temp= JSONObject.parseObject(i.toString());
                if(Double.parseDouble(temp.getString("score"))>0.5)
                {
                    System.out.println("double"+Double.parseDouble(temp.getString("score")));
                    if(temp.getString("brief").matches("[0-9]+"))
                    {

                        res.add(Integer.parseInt(temp.getString("brief")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}