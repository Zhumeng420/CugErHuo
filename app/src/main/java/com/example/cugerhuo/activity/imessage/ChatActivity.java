package com.example.cugerhuo.activity.imessage;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.Msg;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.CreatTradeActivity;
import com.example.cugerhuo.activity.adapter.MsgAdapter;
import com.example.cugerhuo.tools.MyToast;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final MyHandler MyHandler =new MyHandler(Looper.getMainLooper());

    /**
     * 聊天对象
     */
    private PartUserInfo chatUser=new PartUserInfo();
    /**立即交易*/
    private LinearLayout tradeConfirm;
    /**是否从商品详情页跳过来的*/
    private int iWant;
    /**交易模块*/
    private LinearLayout tradeLayout;

    /**
     * 消息通知
     */
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mNotificationBuilder;

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
        tradeConfirm = findViewById(R.id.trade_confirm);
        tradeConfirm.setOnClickListener(this);
        tradeLayout = findViewById(R.id.trade);
        /**
         * 系统消息通知实例
         * @time 2023/05/01
         */
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser= (PartUserInfo) intent.getSerializableExtra("chatUser");
        iWant = (int)intent.getSerializableExtra("iWant");

        if(iWant==1){
            tradeLayout.setVisibility(View.VISIBLE);
        }

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

        /**
         * 以下是接收消息的示例
         */
        Observer<List<IMMessage>> incomingMessageObserver =
                messages -> {
                    if(messages.get(0).getContent()!=""){
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_RECEIVED));
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                msg.arg1 = 1;
                                msg.obj=messages.get(0).getContent();
                                MyHandler.sendMessage(msg);
                            }
                        }).start();
                        //MyToast.toast(ChatActivity.this,messages.toString(),3);
                        /**
                         * 消息的系统提示
                         * @author 朱萌
                         * @time 2023/05/01
                         */

                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

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

        /**
         * 查询历史消息，比anchor时间更早的一万条消息
         * @author 唐小莉
         * @time 2023/5/1
         */
        IMMessage anchor = MessageBuilder.createEmptyMessage("cugerhuo"+chatUser.getId(), SessionTypeEnum.P2P, System.currentTimeMillis());
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                10000, true).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                for(int i=0;i<result.size();i++){

                    /**
                     * 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                     */
                    if(!Objects.equals(result.get(i).getContent(), "")){
                        /**
                         * 发出的消息
                         */
                        if(result.get(i).getDirect()==MsgDirectionEnum.Out){
                            msgList.add(new Msg(result.get(i).getContent(),Msg.TYPE_SEND));

                        }
                        /**
                         * 接收到的消息
                         */
                        if(result.get(i).getDirect()==MsgDirectionEnum.In){
                            msgList.add(new Msg(result.get(i).getContent(),Msg.TYPE_RECEIVED));
                        }
                    }
                }
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        });
    }
    /**
     * 消息发送接收，异步更新UI
     * @author 施立豪
     * @time 2023/4/19
     */
    private class MyHandler extends Handler {
        public MyHandler(Looper mainLooper) {

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    //点击跳转事件
                    Intent newIntent = new Intent(ChatActivity.this,MessageActivity.class);
                    @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pi = PendingIntent.getActivity(ChatActivity.this,0,newIntent,PendingIntent.FLAG_CANCEL_CURRENT);
                    //初始化消息通知
                    mNotificationBuilder=new NotificationCompat.Builder(ChatActivity.this)
                            .setContentTitle("收到了一条新消息")
                            .setContentText((CharSequence) msg.obj)
                            .setWhen(System.currentTimeMillis())
                            .setColor(getResources().getColor(R.color.colorPrimary))
                            .setSmallIcon(R.drawable.logo2)
                            .setContentIntent(pi)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo2));
                    //通知
                    mNotificationManager.notify(1, mNotificationBuilder.build());
                    break;
                default:
                    break;
            }}}

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
                String text="";
                /**
                 * 如果输入框不为空，进行发送
                 */
                if(!inputText.getText().toString().equals("")){
                    text = inputText.getText().toString();
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
                }


                break;
            }
            case R.id.trade_confirm:
                Intent intent=new Intent(ChatActivity.this, CreatTradeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            default:
                break;
        }
    }

    private List<Msg> getData(){
        List<Msg> list = new ArrayList<>();
        if(list.size()!=0){list.clear();}
        return list;
    }

}