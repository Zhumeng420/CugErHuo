package com.example.cugerhuo.activity.imessage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.Msg;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.AddressManageActivity;
import com.example.cugerhuo.activity.CreatTradeActivity;
import com.example.cugerhuo.activity.EditAddressActivity;
import com.example.cugerhuo.activity.TradeDetailActivity;
import com.example.cugerhuo.activity.adapter.MsgAdapter;
import com.example.cugerhuo.activity.session.CustomAttachParser;
import com.example.cugerhuo.activity.session.CustomAttachment;
import com.example.cugerhuo.activity.session.MyOrderAttachment;
import com.example.cugerhuo.activity.session.ToBeConfirmedAttachment;
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
    private ImageView speakImg;
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
    /**立即交易*/
    private LinearLayout tradeConfirm;
    /**是否从商品详情页跳过来的*/
    private int iWant;
    /**交易模块*/
    private LinearLayout tradeLayout;
    /**是否发送交易订单*/
    private int isTrade;
    private final ChatActivity.MyHandler MyHandler =new ChatActivity.MyHandler();
    /**是否确认订单*/
    private int confirmTrade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (NIMUtil.isMainProcess(this)) {
            // 监听的注册，必须在主进程中。
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        }
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
        speakImg = findViewById(R.id.speak);
        speakImg.setOnClickListener(this);


        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser= (PartUserInfo) intent.getSerializableExtra("chatUser");
        if(intent.getSerializableExtra("iWant")!=null){
            iWant = (int)intent.getSerializableExtra("iWant");
        }else{
            iWant = 0;
        }

        /**
         * 待确认订单
         */
        if(intent.getSerializableExtra("isTrade")!=null){
            isTrade = (int)intent.getSerializableExtra("isTrade");
        }else{
            isTrade = 0;
        }

        /**
         * 已确认订单
         */
        if(intent.getSerializableExtra("confirmTrade")!=null){
            confirmTrade = (int)intent.getSerializableExtra("confirmTrade");
        }else{
            confirmTrade = 0;
        }

        /**展示顶部交易模块*/
        tradeLayout.setVisibility(View.VISIBLE);
//        if(iWant==1){tradeLayout.setVisibility(View.VISIBLE);}

        /**发送订单*/
        if(isTrade==1){
            int tradeId = (int)intent.getSerializableExtra("tradeId");
            createTrade(tradeId);
        }

        /**发送订单确认信息*/
        if(confirmTrade==1){
            int tradeId = (int)intent.getSerializableExtra("tradeId");
            SendConfirmTrade(tradeId);
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
                        if(messages.get(0).getAttachment()!=null){
                            CustomAttachment attachment = (CustomAttachment)messages.get(0).getAttachment();
                            int type = attachment.getType();
                            if(type==10003){
                                msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_CONFIRM_CARD));
                            }else if(type==10002){
                                msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_RECEIVED_CARD));
                            }
//                            if(messages.get(0).getAttachment()==new ToBeConfirmedAttachment()){
//                                msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_CONFIRM_CARD));
//                            }else if(messages.get(0).getAttachment()==new MyOrderAttachment()){
//                                msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_RECEIVED_CARD));
//                            }
                        }else{
                            // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                            msgList.add(new Msg(messages.get(0).getContent(),Msg.TYPE_RECEIVED));
                        }
                        adapter.notifyItemInserted(msgList.size()-1);
                        msgRecyclerView.scrollToPosition(msgList.size()-1);
                        MyToast.toast(ChatActivity.this,messages.toString(),3);
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
                          if(result.get(i).getAttachStr()!=null){
                              if(result.get(i).getAttachStr().indexOf("10003")!=-1){
//                                  msgList.add(new Msg(result.get(i).getAttachStr(),Msg.TYPE_SEND));
                              }else if(result.get(i).getAttachStr().indexOf("10002")!=-1){
                                  msgList.add(new Msg(result.get(i).getAttachStr(),Msg.TYPE_SEND_CARD));
                              }
                          }else{
                              msgList.add(new Msg(result.get(i).getContent(),Msg.TYPE_SEND));
                          }
                      }
                      /**
                       * 接收到的消息
                       */
                      if(result.get(i).getDirect()==MsgDirectionEnum.In){
                          if(result.get(i).getAttachStr()!=null){
                              if(result.get(i).getAttachStr().indexOf("10003")!=-1){
                                  msgList.add(new Msg(result.get(i).getAttachStr(),Msg.TYPE_CONFIRM_CARD));}
                              else if(result.get(i).getAttachStr().indexOf("10002")!=-1){
                                  msgList.add(new Msg(result.get(i).getAttachStr(),Msg.TYPE_RECEIVED_CARD));
                              }
                          }else{
                              msgList.add(new Msg(result.get(i).getContent(),Msg.TYPE_RECEIVED));
                          }
                      }
                  }
                }
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        });

        // 开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            MyHandler.sendMessage(msg);
        }).start();
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
                intent.putExtra("chatUser",chatUser);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
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

    /**
     * 发送订单待确认信息
     * @Author: 李柏睿
     * @Time: 2023/5/2 0:23
     */
    public void createTrade(int tradeId){
        final int returnInt;

        // 给该账号发送消息
        String account = "cugerhuo"+chatUser.getId();
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 消息的配置选项
        CustomMessageConfig config = new CustomMessageConfig();
        // 该消息保存到服务器
        config.enableHistory = true;
        // 该消息漫游
        config.enableRoaming = true;
        // 该消息同步
        config.enableSelfSync = true;
        ToBeConfirmedAttachment customerAttachment = new ToBeConfirmedAttachment();
        //创建IMMessage，其中sessionId代表会话ID，sessionType会话类型，content代表消息内容，customerAttachment自定义消息
        IMMessage customMessage = MessageBuilder.createCustomMessage(account, sessionType, customerAttachment);
        customMessage.setConfig(config);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(customMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                MyToast.toast(ChatActivity.this,"订单消息发送成功",3);
            }

            @Override
            public void onFailed(int code) {
                MyToast.toast(ChatActivity.this,"订单消息发送失败",1);
            }
            @Override
            public void onException(Throwable exception) {
                MyToast.toast(ChatActivity.this,"订单消息发送异常",1);
            }
        });

    }

    /**
     * 发送订单已确认信息
     * @Author: 李柏睿
     * @Time: 2023/5/2 0:23
     */
    public void SendConfirmTrade(int tradeId){
        // 给该账号发送消息
        String account = "cugerhuo"+chatUser.getId();
        // 以单聊类型为例
        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        // 消息的配置选项
        CustomMessageConfig config = new CustomMessageConfig();
        // 该消息保存到服务器
        config.enableHistory = true;
        // 该消息漫游
        config.enableRoaming = true;
        // 该消息同步
        config.enableSelfSync = true;
        MyOrderAttachment customerAttachment = new MyOrderAttachment();
        //创建IMMessage，其中sessionId代表会话ID，sessionType会话类型，content代表消息内容，customerAttachment自定义消息
        IMMessage customMessage = MessageBuilder.createCustomMessage(account, sessionType, customerAttachment);
        customMessage.setConfig(config);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(customMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Msg customMsg = new Msg(String.valueOf(tradeId),Msg.TYPE_SEND_CARD);
                msgList.add(customMsg);
                MyToast.toast(ChatActivity.this,"订单确认消息发送成功",3);
            }

            @Override
            public void onFailed(int code) {
                MyToast.toast(ChatActivity.this,"订单确认消息发送失败",1);
            }
            @Override
            public void onException(Throwable exception) {
                MyToast.toast(ChatActivity.this,"订单确认消息发送异常",1);
            }
        });

    }

    /**
     * 消息发送接收，异步更新UI
     * @Author: 李柏睿
     * @Time: 2023/5/1
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){

                case 1:
                    /**
                     * 点击待确认订单跳转
                     */
                    adapter.setOnItemClickListener(new MsgAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int flag = 1;
                            Intent intent=new Intent(ChatActivity.this, TradeDetailActivity.class);
                            intent.putExtra("flag",flag);
                            intent.putExtra("chatUser",chatUser);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                            finish();
                        }
                    });

                    adapter.setOnItemDetailClickListener(new MsgAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int isConfirm = 1;
                            Intent intent=new Intent(ChatActivity.this, TradeDetailActivity.class);
                            intent.putExtra("isConfirm",isConfirm);
                            intent.putExtra("chatUser",chatUser);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                            finish();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

}