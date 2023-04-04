package com.example.cugerhuo.Activity.IMessageActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cugerhuo.DataAccess.User.ImOperate;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.MyToast;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.util.NIMUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.opentracing.util.GlobalTracer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity  implements  View.OnClickListener  {
private
    Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        /**
         * 即时通讯初始化
         * @author 朱萌
         * @time 2023/4/3 21：14
         */
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将进行自动登录）。不能对初始化语句添加进程判断逻辑。
        NIMClient.init(this,  null, null);
        // ... your codes
        LoginInfo info = new LoginInfo("test1","123456");
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        MyToast.Toast(ChatActivity.this,"登录成功",3);
                        // your code
                        /**
                         *监听数据同步状态
                         */
                        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
                            @Override
                            public void onEvent(LoginSyncStatus status) {
                                if (status == LoginSyncStatus.BEGIN_SYNC) {
                                    MyToast.Toast(ChatActivity.this,"数据同步开始",3);
                                } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
                                    MyToast.Toast(ChatActivity.this,"数据同步完成",3);
                                }
                            }
                        }, true);
                        /**
                         * 以下是接收消息的示例
                         */
                        Observer<List<IMMessage>> incomingMessageObserver =
                                messages -> {
                                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                                    TextView recevie=findViewById(R.id.textView);
                                    recevie.setText(messages.get(0).getFromAccount()+":"+messages.get(0).getContent());
                                    MyToast.Toast(ChatActivity.this,messages.toString(),3);
                                };
                        NIMClient.getService(MsgServiceObserve.class)
                                .observeReceiveMessage(incomingMessageObserver, true);
                    }
                    @Override
                    public void onFailed(int code) {
                        if (code == 302) {
                            MyToast.Toast(ChatActivity.this,"账号密码错误",1);
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

        /**
         * 监听登录状态
         */
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode> () {
                    public void onEvent(StatusCode status) {
                        //获取状态的描述
                        String desc = status.getDesc();
                        if (status.wontAutoLogin()) {
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                        }
                    }
                }, true);


        // 使用 `NIMUtil` 类可以进行主进程判断。
        // boolean mainProcess = NIMUtil.isMainProcess(context)
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            /**
             * 发送消息点击事件
             */
            Send=findViewById(R.id.send);
            Send.setOnClickListener(this);

        }

    }


    /**
     * UI的点击实际处理
     * @author 朱萌
     * @time 2023/4/4
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:{
                //MyToast.Toast(ChatActivity.this,"你妈的",3);
                /**
                 * 以下是往一个账号发送消息的示例
                 */
                // 该帐号为示例
                String account = "test1";
                // 以单聊类型为例
                SessionTypeEnum sessionType = SessionTypeEnum.P2P;
                String text = "hello world";
                // 创建一个文本消息
                IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, text);
                // 消息的配置选项
                CustomMessageConfig config = new CustomMessageConfig();
                // 该消息保存到服务器
                config.enableHistory = true;
                // 该消息漫游
                config.enableRoaming = true;
                // 该消息同步
                config.enableSelfSync = true;
                textMessage.setConfig(config);

                // 发送给对方
                NIMClient.getService(MsgService.class).sendMessage(textMessage, false).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        MyToast.Toast(ChatActivity.this,"消息发送成功",3);
                    }

                    @Override
                    public void onFailed(int code) {
                        MyToast.Toast(ChatActivity.this,"消息发送失败",1);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        MyToast.Toast(ChatActivity.this,"消息发送异常",1);
                    }
                });
            }
        }
    }

}