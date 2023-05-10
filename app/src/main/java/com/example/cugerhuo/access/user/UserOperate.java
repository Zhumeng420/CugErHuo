package com.example.cugerhuo.access.user;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于调用用户类的接口
 * @author 施立豪
 * @time 2023/3/18
 */
public class UserOperate {
    /**
     * 调用服务端的插入手机号到redis布隆过滤器接口
     * @param phone 手机号
     * @param context 用于获取反射常量
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean insertPhoneBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertPhoneToBloom);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
         String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端的插入手机号到redis布隆过滤器接口
     * @param qqOpenId 手机号
     * @param context 用于获取反射常量
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean insertQqBloom(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertQqToBloom);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过手机号向mysql插入数据的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean insertByPhone(String phone, String username1, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */

        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertUserByPhone);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        String username=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone+"&"+username+"="+username1;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过qq号向mysql插入数据的接口
     * @param qqOpenId 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean insertByQq(String qqOpenId, String username, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */

        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.InsertUserByQq);
        String qq=context.getString(R.string.Qq);
        String userName=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId+"&"+userName+"="+username;
         //构建Request，将其传入Request请求中
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }
    /**
     * 调用服务端通过手机号查询redis是否存在的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean isPhoneExistBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsPhoneExistInBloom);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过qq号查询redis是否存在的接口
     * @param qqOpenId qq号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean isQqExistBloom(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsQqExistInBloom);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }
    /**
     * 调用服务端通过手机号查询redis，手机号是否被封的接口
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/19
     */
    public static boolean isPhoneBanedBloom(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsPhoneBanedInBloom);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }
    /**
     * 调用服务端通过qq号查询redis，qq号是否被封的接口
     * @param qqOpenId 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static boolean isQqBanedBloom(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.IsQqBanedInBloom);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isInserted=false;
        try {
            response = okHttpClient.newCall(request).execute();
            isInserted="true".equals(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return isInserted;
    }

    /**
     * 调用服务端通过手机号查询用户id
     * @param phone 手机号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/26
     */
    public static int getId(String phone, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetIdByPhone);
        String phoneNumber=context.getString(R.string.PhoneNumber);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+phoneNumber+"="+phone;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        int result =-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 调用服务端通过qq号查询用户id
     * @param qqOpenId qq号
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/28
     */
    public static int getQqId(String qqOpenId, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.GetIdByQq);
        String qq=context.getString(R.string.Qq);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router+"?"+qq+"="+qqOpenId;
        //构建Request，将其传入Request请求中

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        int result
                =-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 插入id和用户名到图数据库
     * @param username 用户名
     * @param id 用户id
     * @param context 获取映射文件
     * @return 是否成功
     * @author 施立豪
     * @time 2023/3/26
     */
    public static boolean insertUserToTu(String username, int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.InsertUserToTu);
        String name=context.getString(R.string.Name);
        String uid=context.getString(R.string.UserId);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        builder.add(name,username);
        FormBody body = builder.build();
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
     * 获取用户关注数
     * @param id 用户id
     * @param context 获取映射文件
     * @return 数量
     * @author 施立豪
     * @time 2023/3/27
     */
    public static int getFocusNum(int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.FocusNum);
        String uid=context.getString(R.string.UserId);

        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        int result=-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 获取用户粉丝
     * @param id 用户id
     * @param context 获取映射文件
     * @return 数量
     * @author 施立豪
     * @time 2023/3/27
     */
    public static int getFansNum(int id, Context context)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.FansNum);
        String uid=context.getString(R.string.UserId);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        //循环form表单，将表单内容添加到form builder中
        //构建formBody，将其传入Request请求中
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        int result=-1;
        try {
            response = okHttpClient.newCall(request).execute();
            result= Integer.parseInt(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 获取关注列表用户id
     * @param id 登录用户id
     * @param context 获取映射文件
     * @return 返回关注列表id
     * @author 唐小莉
     * @time 2023/4/11 19:15
     */
    public static List<Integer> getConcernId(int id, Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.Focus);
        String uid=context.getString(R.string.UserId);

        String url="http://"+ip+"/"+router;
        /** 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        /**
         * 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        List<Integer> result=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            result= JSONArray.parseArray(response.body().string(),Integer.class);
        } catch (IOException e) {
            e.printStackTrace();
        }return result;

    }

    /***
     * 取消关注
     * @param startId1 本用户id
     * @param endId1 取消关注用户id
     * @param context 获取映射文件
     * @return 返回是否取消成功
     * @author 唐小莉
     * @time 2023/4/13
     */
    public static Boolean getIfDeleteConcern(int startId1, int endId1, Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.DeleteFocusUser);
        String startId=context.getString(R.string.startId);
        String endId=context.getString(R.string.endId);

        String url="http://"+ip+"/"+router;
        /** 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(startId, String.valueOf(startId1));
        builder.add(endId,String.valueOf(endId1));
        /**
         * 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).delete(body).build();
        Response response = null;
        boolean isDelete=false;
        List<Integer> result=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            JSONObject obj=new JSONObject(response.body().string());
            if("200".equals(obj.getString("code"))){
                isDelete=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isDelete;
    }

    /**
     * 关注用户
     * @param startId1 本用户id
     * @param endId1 被关注用户id
     * @param context 获取映射文件
     * @return 返回是否关注成功
     * @author 唐小莉
     * @time 2023/4/１９
     */
    public static Boolean setConcern(int startId1, int endId1, Context context){

        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.setFocusUser);
        String startId=context.getString(R.string.startId);
        String endId=context.getString(R.string.endId);

        String url="http://"+ip+"/"+router;
        /** 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(startId, String.valueOf(startId1));
        builder.add(endId,String.valueOf(endId1));
        /**
         * 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            System.out.println("hhhhhhhhhhhhhhgggggggg"+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 获取推荐关注用户的id列表
     * @param id 用户id
     * @param context 获取映射文件
     * @return 返回推荐用户id列表
     * @author 唐小莉
     * @time 2023/4/25
     */
    public static List<Integer> getRecommend(int id, Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.UserRecommend);
        String userId=context.getString(R.string.UserId);

        String url="http://"+ip+"/"+router+"?"+userId+"="+id;
        /** 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(userId, String.valueOf(id));

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        List<Integer> listId=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            JSONObject obj=new JSONObject(response.body().string());
            listId=JSONArray.parseArray(obj.getString("object"),Integer.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listId;
    }

    /**
     * 获取粉丝列表id
     * @param id 用户id
     * @param context 获取映射文件
     * @return 返回粉丝id列表
     * @author 唐小莉
     * @time 2023/4/25
     */
    public static List<Integer> getFansId(int id,Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.Fans);
        String uid=context.getString(R.string.UserId);

        String url="http://"+ip+"/"+router;
        /** 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody.Builder builder = new FormBody.Builder();
        builder.add(uid, String.valueOf(id));
        /**
         * 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        List<Integer> result=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            result= JSONArray.parseArray(response.body().string(),Integer.class);
        } catch (IOException e) {
            e.printStackTrace();
        }return result;
    }

    /**
     * 判断是否互相关注
     * @param id1
     * @param id2
     * @param context
     * @return
     * @author 唐小莉
     * @time 2023/4/25
     */
    public static boolean isAllFocus(int id1,int id2,Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.Tuip);
        String router=context.getString(R.string.AllFocus);
        String startId=context.getString(R.string.startId);
        String endId=context.getString(R.string.endId);

        String url="http://"+ip+"/"+router+"?"+startId+"="+id1+"&"+endId+"="+id2;
        /** 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(startId, String.valueOf(id1));
        builder.add(endId,String.valueOf(id2));

        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        boolean isFocus=false;
        List<Integer> result=new ArrayList<>();
        try {
            response = okHttpClient.newCall(request).execute();
            response.body();
            JSONObject obj=new JSONObject(response.body().string());
            if("true".equals(obj.getString("object"))){
                isFocus=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isFocus;
    }

    /**
     * 用户模糊搜索
     * @param userName 搜索用户名
     * @param context 获取映射文件
     * @return 返回用户列表
     * @author 唐小莉
     * @time 2023/5/4
     */
    public static List<PartUserInfo> getSearchUser(String userName,Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        List<PartUserInfo> partUserInfos=new ArrayList<>();
        /**
         * 获取XML文本
         */
        String ip=context.getString(R.string.ip);
        String router=context.getString(R.string.searchUser);
        String username=context.getString(R.string.Username);
        /**
         * 发送请求
         */
        String url="http://"+ip+"/"+router;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add(username, String.valueOf(userName));
        /**
         * 循环form表单，将表单内容添加到form builder中
         * 构建formBody，将其传入Request请求中
         */

        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            partUserInfos=JSONArray.parseArray(response.body().string(),PartUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return partUserInfos;
    }

}
