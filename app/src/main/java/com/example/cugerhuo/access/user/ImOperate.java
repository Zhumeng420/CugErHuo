package com.example.cugerhuo.access.user;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * IM即时通讯操作
 * @author 朱萌
 * @time 2023/4/4 9:41
 */
public class ImOperate {
    /**
     * 注册云信账号
     * @author
     * @return
     */
    public boolean Resgiser() throws JSONException {
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accid", "test");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("token", "123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestJsonBody = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json")
        );
        Request postRequest = new Request.Builder()
                .url("https://api.netease.im/nimserver/user/create.action")
                .post(requestJsonBody)
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 登录网易云信账号
     * @author 朱萌
     * @time 2023/4/3 21：14
     */
    public void DoLogin() throws JSONException {
        System.out.println("测试！测试！测试！测试！测试！测试！");
        LoginInfo info = new LoginInfo("test1","123456");
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        System.out.println("登录成功");
                        // your code
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302) {
                            System.out.println("登录失败");
                            // your code
                        } else {
                            // your code
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        // your code
                    }
                };

        //执行手动登录
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);
    }

//    public void GetMessage(){
//        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
//            @Override
//            public void onEvent(LoginSyncStatus status) {
//                if (status == LoginSyncStatus.BEGIN_SYNC) {
//                    System.out.println("数据同步开始");
//                } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
//                    System.out.println("数据同步结束");
//                }
//            }
//        }, register);
//    }

}
