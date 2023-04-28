package com.example.cugerhuo.activity.imessage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.Msg;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.adapter.MsgAdapter;
import com.example.cugerhuo.tools.MyToast;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天
 * @Author: 朱萌
 * @Time: 2023/4/11
 */
public class ChatActivity extends AppCompatActivity  implements  View.OnClickListener  {

    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText inputText;
    private Button send;
    private LinearLayoutManager layoutManager;
    private MsgAdapter adapter;
    private ImageView returnImg;
    /**
     * 聊天对象头像
     * 聊天对象昵称
     */
    private RoundedImageView chatUserImg;
    private TextView  chatUserName;
    /**
     * 聊天对象
     */
    private PartUserInfo chatUser=new PartUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        returnImg = findViewById(R.id.return_chat);
        chatUserImg=findViewById(R.id.chatUserImg);
        chatUserName=findViewById(R.id.chatUserName);
        returnImg.setOnClickListener(this);

        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser= (PartUserInfo) intent.getSerializableExtra("chatUser");
        if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
        {
            chatUserImg.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
        }
        /**
         * 初始化聊天对象信息
         */
        chatUserName.setText(chatUser.getUserName());

        layoutManager = new LinearLayoutManager(this);
        adapter = new MsgAdapter(msgList = getData(),chatUser,ChatActivity.this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);
        /**
         * 即时通讯初始化
         * @author 朱萌
         * @time 2023/4/3 21：14
         */
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将进行自动登录）。不能对初始化语句添加进程判断逻辑。
        NIMClient.init(this,  null, null);
        // ... your codes
        LoginInfo info = new LoginInfo("cugerhuo12","123456");
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        MyToast.toast(ChatActivity.this,"登录成功",3);
                        // your code
                        /**
                         *监听数据同步状态
                         */
                        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
                            @Override
                            public void onEvent(LoginSyncStatus status) {
                                if (status == LoginSyncStatus.BEGIN_SYNC) {
                                    //MyToast.toast(ChatActivity.this,"数据同步开始",3);
                                } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
                                    //MyToast.toast(ChatActivity.this,"数据同步完成",3);
                                }
                            }
                        }, true);
                        /**
                         * 以下是接收消息的示例
                         */
                        Observer<List<IMMessage>> incomingMessageObserver =
                                messages -> {
                                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
//                                    TextView recevie=findViewById(R.id.textView);
//                                    recevie.setText(messages.get(0).getFromAccount()+":"+messages.get(0).getContent());
                                    msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_RECEIVED));
                                    adapter.notifyItemInserted(msgList.size()-1);
                                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                                    MyToast.toast(ChatActivity.this,messages.toString(),3);
                                };
                        NIMClient.getService(MsgServiceObserve.class)
                                .observeReceiveMessage(incomingMessageObserver, true);
                    }
                    @Override
                    public void onFailed(int code) {
                        if (code == 302) {
                            MyToast.toast(ChatActivity.this,"账号密码错误",1);
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
        /**执行手动登录*/
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);

        /**
         * 监听登录状态
         */
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode> () {
                    @Override
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
            send =findViewById(R.id.send);
            send.setOnClickListener(this);

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
            case R.id.return_chat:
                finish();
                break;
            case R.id.send:{
                //MyToast.Toast(ChatActivity.this,"你妈的",3);
                /**
                 * 以下是往一个账号发送消息的示例
                 *
                 */
                // 给该账号发送消息
                String account = "cugerhuo"+chatUser.getId();
                // 以单聊类型为例
                SessionTypeEnum sessionType = SessionTypeEnum.P2P;
                String text = inputText.getText().toString();
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
                        //MyToast.toast(ChatActivity.this,"消息发送成功",3);
                        String content = inputText.getText().toString();
                        if(!content.equals("")) {
                            msgList.add(new Msg(content,Msg.TYPE_SEND));
                            adapter.notifyItemInserted(msgList.size()-1);
                            msgRecyclerView.scrollToPosition(msgList.size()-1);
                            inputText.setText("");//清空输入框中的内容
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        MyToast.toast(ChatActivity.this,"消息发送失败",1);
                    }
                    @Override
                    public void onException(Throwable exception) {
                        MyToast.toast(ChatActivity.this,"消息发送异常",1);
                    }
                });
                break;
            }
            default:
                break;
        }
    }

    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        if(list.size()!=0){list.clear();}
        list.add(new Msg("Hello",Msg.TYPE_RECEIVED));
        return list;
    }

}