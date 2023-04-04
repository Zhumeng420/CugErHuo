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
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
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
        System.out.println("测试！测试！测试！测试！测试！测试！");
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将进行自动登录）。不能对初始化语句添加进程判断逻辑。
        NIMClient.init(this,  loginInfo(), null);
        // ... your codes

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
            /**
             * 以下是接收消息的示例
             */
            Observer<List<IMMessage>> incomingMessageObserver =
                    messages -> {
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        TextView recevie=findViewById(R.id.textView);
                        recevie.setText(messages.toString());
                        MyToast.Toast(ChatActivity.this,messages.toString(),3);
                    };
            NIMClient.getService(MsgServiceObserve.class)
                    .observeReceiveMessage(incomingMessageObserver, true);



        }

    }

    private LoginInfo loginInfo() {
        LoginInfo info = new LoginInfo("test1","123456");
        return info;
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
                String account = "test2";
                // 以单聊类型为例
                SessionTypeEnum sessionType = SessionTypeEnum.P2P;
                String text = "hello world";
                // 创建一个文本消息
                IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, text);
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