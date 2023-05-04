package com.example.cugerhuo.activity.imessage;

import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commerce.Commerce;
import com.example.cugerhuo.access.commerce.CommerceOperate;
import com.example.cugerhuo.access.user.Msg;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.CreatTradeActivity;
import com.example.cugerhuo.activity.LocationClockActivity;
import com.example.cugerhuo.activity.LocationDetailActivity;
import com.example.cugerhuo.activity.TradeDetailActivity;
import com.example.cugerhuo.activity.adapter.MsgAdapter;
import com.example.cugerhuo.activity.session.CustomAttachParser;
import com.example.cugerhuo.activity.session.CustomAttachment;
import com.example.cugerhuo.activity.session.MyOrderAttachment;
import com.example.cugerhuo.activity.session.ToBeConfirmedAttachment;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.tools.entity.TradeInfo;
import com.example.cugerhuo.views.RecordButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private Commerce commerce;
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
    /**
     * 商品信息
     */
    private Commodity charCommodity=new Commodity();
    /**
     * 订单信息
     */
    private TradeInfo tradeInfo;
    /**
     * 商品图片
     */
    private RoundedImageView tradeImages;
    private TextView tradePrice;
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
    /**点击更多*/
    private ImageView chatMore;
    /**底部更多*/
    private LinearLayout moreFunction;
    /**选择图片发送*/
    private LinearLayout selectPic;
    /**选择地理位置发送*/
    private LinearLayout selectPosition;
    /**按住说话*/
    private RecordButton recordButton;
    /**输入线性布局*/
    private LinearLayout editLayout;
    /**是否发送地理位置*/
    private int isSendLocation;
    /**打卡*/
    private LinearLayout clockLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (NIMUtil.isMainProcess(this)) {
            // 监听的注册，必须在主进程中。
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        }

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        msgRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof RecyclerView){
                    if(moreFunction.getVisibility()!=View.GONE){
                        moreFunction.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        /**动态申请语音权限*/
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECORD_AUDIO);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0x20);
        }

        /**动态申请定位权限*/
        int hasWriteStoragePermission2 = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)&ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteStoragePermission2 == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x20);
        }
        tradeImages=findViewById(R.id.img_trade_images);
        tradePrice=findViewById(R.id.trade_price);
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
        chatMore = findViewById(R.id.chat_more);
        chatMore.setOnClickListener(this);
        moreFunction = findViewById(R.id.more_function);
        selectPic = findViewById(R.id.chat_send_pic);
        selectPic.setOnClickListener(this);
        recordButton = findViewById(R.id.btnAudio);
        editLayout = findViewById(R.id.edit_msg);
        selectPosition = findViewById(R.id.chat_send_position);
        selectPosition.setOnClickListener(this);
        clockLocation = findViewById(R.id.chat_clock);
        clockLocation.setOnClickListener(this);
        ((RecordButton) recordButton).setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                Log.e("TAG", "录音结束回调");
                File file = new File(audioPath);
                if (file.exists()) {
                    sendAudioMessage(audioPath,time);
                    Log.e("TAG", "录音成功！");
                }
            }
        });

        /**监听输入框*/
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (inputText.getText().length() > 0) {
                    send.setVisibility(View.VISIBLE);
                    chatMore.setVisibility(View.GONE);

                } else {
                    send.setVisibility(View.GONE);
                    chatMore.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    send.setVisibility(View.GONE);
                    chatMore.setVisibility(View.VISIBLE);
                } else {
                    send.setVisibility(View.VISIBLE);
                    chatMore.setVisibility(View.GONE);
                }
            }
        });

        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser = (PartUserInfo) intent.getSerializableExtra("chatUser");
        /**
         * 获取商品信息
         */
        if(intent.getSerializableExtra("chatCommodity")!=null){

            charCommodity= (Commodity) intent.getSerializableExtra("chatCommodity");
            if(charCommodity!=null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       commerce= CommerceOperate.getCommerce(charCommodity.getId(),charCommodity.getUserId(), UserInfo.getid(),ChatActivity.this);
                        Message msg = Message.obtain();
                        msg.arg1 = 2;
                        MyHandler.sendMessage(msg);
                    }
                }).start();
            tradePrice.setText(charCommodity.getPrice().toString());
            if(!"".equals(charCommodity.getUrl1()))
            {String url1=charCommodity.getUrl1();
                String []urls=url1.split(";");
                if(urls.length>0){
                    url1=urls[0];}
                int length=urls.length;
                String result[]=new String[length];
                result[length-1]=urls[length-1];
// 从后往前依次减去后面一个元素
                if(length>1){
                    for (int i = length - 2; i >= 0; i--) {
                        String current = urls[i];
                        String next = result[i + 1];
                        int index = current.lastIndexOf(next);
                        if(index>0){
                            result[i ] = current.substring(0, index);}
                        else
                        {
                            result[i]=current;
                        }
                    }}
// 将第一个元素赋值给结果数组
                url1=result[0];
                String newUrl1 = getSandBoxPath(ChatActivity.this) + url1;
                System.out.println("imager2"+url1);
                File f = new File(newUrl1);
                if (f.exists())
                {
                    tradeImages.setImageURI(Uri.fromFile(f));
                }
            }
        }}

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
        if(iWant==1){tradeLayout.setVisibility(View.VISIBLE);}

        /**发送订单*/
        if(isTrade==1){
            int tradeId = (int)intent.getSerializableExtra("tradeId");
            createTrade(tradeId);
        }

        /**发送订单确认信息*/
        if(confirmTrade==1){
            int tradeId = (int)intent.getSerializableExtra("tradeId");
            TradeInfo tradeInfo=(TradeInfo)intent.getSerializableExtra("tradeInfo") ;
            SendConfirmTrade(tradeInfo);
        }

        /**
         * 地理信息
         */
        if(intent.getSerializableExtra("isSendLocation")!=null){
            isSendLocation = (int)intent.getSerializableExtra("isSendLocation");
        }else{
            isSendLocation = 0;
        }

        if(isSendLocation==1){
            double lat = (double) intent.getSerializableExtra("lat");
            double lng = (double) intent.getSerializableExtra("lng");
            String poiName = (String) intent.getSerializableExtra("poiName");
            SendPosition(lat,lng,poiName);

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
                            if(messages.get(0).getAttachment().toString().indexOf("ImageAttachment")!=-1){
                                Log.e("TAG", "messages.get(0).getAttachment(): " + messages.get(0).getAttachment().toString());
                                ImageAttachment msgAttachment=(ImageAttachment)messages.get(0).getAttachment();
                                String uri=msgAttachment.getThumbUrl();
                                msgList.add(new Msg(uri,Msg.TYPE_RECEIVED_PIC));
                            }else if(messages.get(0).getAttachment().toString().indexOf("AudioAttachment")!=-1){
                                AudioAttachment msgAttachment=(AudioAttachment) messages.get(0).getAttachment();
                                String uri=msgAttachment.getUrl();
                                long timeReceive = msgAttachment.getDuration();
                                msgList.add(new Msg(uri+",and time is"+timeReceive/1000,Msg.TYPE_RECEIVED_AUDIO));
                            }else if(messages.get(0).getAttachment().toString().indexOf("LocationAttachment")!=-1){
                                LocationAttachment locationAttachment = (LocationAttachment) messages.get(0).getAttachment();
                                double lat = locationAttachment.getLatitude();
                                double lng = locationAttachment.getLongitude();
                                String title = locationAttachment.getAddress();
                                msgList.add(new Msg(lng+","+ lat+"and"+title,Msg.TYPE_RECEIVED_LOCATION));
                            }else{//自定义消息消息
                                CustomAttachment attachment = (CustomAttachment)messages.get(0).getAttachment();
                                int type = attachment.getType();
                                if(type==10003){
                                    ToBeConfirmedAttachment attachment1= (ToBeConfirmedAttachment)attachment;
                                    String tradeString=attachment1.getContent();
                                    System.out.println("tradeinfo"+tradeString);
                                    tradeInfo=JSON.parseObject(tradeString,TradeInfo.class);
                                    msgList.add(new Msg(tradeString,Msg.TYPE_CONFIRM_CARD));
                                }else if(type==10002){
                                    MyOrderAttachment attachment1= (MyOrderAttachment)attachment;
                                    String tradeString=attachment1.getContent();
                                    System.out.println("tradeinfo"+tradeString);
                                    tradeInfo=JSON.parseObject(tradeString,TradeInfo.class);
                                    msgList.add(new Msg(tradeString,Msg.TYPE_RECEIVED_CARD));
                                }
                            }

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
                                  /**
                                   * 读取历史消息中的订单信息
                                   */
                                  msgList.add(new Msg(result.get(i).getAttachStr(),Msg.TYPE_SEND));
                              }else if(result.get(i).getAttachStr().indexOf("10002")!=-1){
                                MyOrderAttachment a= ( MyOrderAttachment)result.get(i).getAttachment();
                                String tradeString=a.getContent();
                                System.out.println("tradeinfo"+tradeString);
                                tradeInfo=JSON.parseObject(tradeString,TradeInfo.class);
                                msgList.add(new Msg(tradeString,Msg.TYPE_SEND_CARD));
                              } else if(result.get(i).getAttachment().toString().indexOf("ImageAttachment")!=-1){
                                  String path = result.get(i).getAttachStr();
                                  String filePath = subString(path,"\"url\":\"","\",\"size\"");
                                  String filePathTo = filePath.replace("\\/","/");
                                  msgList.add(new Msg(filePathTo+"&thumbnail=350x350&imageView",Msg.TYPE_SEND_PIC));
                              } else if(result.get(i).getAttachment().toString().indexOf("AudioAttachment")!=-1){
                                  String path = result.get(i).getAttachStr();
                                  String filePath = subString(path,"\"url\":\"","\",\"size\"");
                                  String filePathTo = filePath.replace("\\/","/");
                                  MediaPlayer mediaPlayer = new MediaPlayer();
                                  try {
                                      mediaPlayer.setDataSource(filePathTo);
                                      mediaPlayer.prepare();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                                  int timeAudio = mediaPlayer.getDuration();
                                  msgList.add(new Msg(filePathTo+",and time is"+timeAudio/1000,Msg.TYPE_SEND_AUDIO));
                              }else if(result.get(i).getAttachment().toString().indexOf("LocationAttachment")!=-1){
                                  String attachStr = result.get(i).getAttachStr();
                                  String lat = subString(attachStr,"\"lat\":",",\"lng\"");
                                  String lng = subString(attachStr,"\"lng\":",",\"title\"");
                                  String title = subString(attachStr,"title\":\"","\"}");
                                  msgList.add(new Msg(lng+","+ lat+"and"+title,Msg.TYPE_SEND_LOCATION));
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
                                  ToBeConfirmedAttachment attachment1= (ToBeConfirmedAttachment)result.get(i).getAttachment();
                                  String tradeString=attachment1.getContent();
                                  System.out.println("tradeinfo"+tradeString);
                                  tradeInfo=JSON.parseObject(tradeString,TradeInfo.class);
                                  msgList.add(new Msg(tradeString,Msg.TYPE_CONFIRM_CARD));
                              }
                              else if(result.get(i).getAttachStr().indexOf("10002")!=-1){
//                                  ToBeConfirmedAttachment attachment1= (ToBeConfirmedAttachment)result.get(i).getAttachment();

                                  MyOrderAttachment attachment1= (MyOrderAttachment)result.get(i).getAttachment();
                                  String tradeString=attachment1.getContent();
                                  System.out.println("tradeinfo"+tradeString);
                                  tradeInfo=JSON.parseObject(tradeString,TradeInfo.class);
                                  msgList.add(new Msg(tradeString,Msg.TYPE_RECEIVED_CARD));

                              }else if(result.get(i).getAttachment().toString().indexOf("ImageAttachment")!=-1){
                                  String path = result.get(i).getAttachStr();
                                  String filePath = subString(path,"\"url\":\"","\",\"size\"");
                                  String filePathTo = filePath.replace("\\/","/");
                                  msgList.add(new Msg(filePathTo+"&thumbnail=350x350&imageView",Msg.TYPE_RECEIVED_PIC));
                              }else if(result.get(i).getAttachment().toString().indexOf("AudioAttachment")!=-1){
                                  String path = result.get(i).getAttachStr();
                                  String filePath = subString(path,"\"url\":\"","\",\"size\"");
                                  String filePathTo = filePath.replace("\\/","/");
                                  MediaPlayer mediaPlayer = new MediaPlayer();
                                  try {
                                      mediaPlayer.setDataSource(filePathTo);
                                      mediaPlayer.prepare();
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }
                                  int timeAudio = mediaPlayer.getDuration();
                                  msgList.add(new Msg(filePathTo+",and time is"+timeAudio/1000,Msg.TYPE_RECEIVED_AUDIO));
                              }else if(result.get(i).getAttachment().toString().indexOf("LocationAttachment")!=-1){
                                  String attachStr = result.get(i).getAttachStr();
                                  String lat = subString(attachStr,"\"lat\":",",\"lng\"");
                                  String lng = subString(attachStr,"\"lng\":",",\"title\"");
                                  String title = subString(attachStr,"title\":\"","\"}");
                                  msgList.add(new Msg(lng+","+ lat+"and"+title,Msg.TYPE_RECEIVED_LOCATION));
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
            case R.id.chat_more:
                moreFunction.setVisibility(View.VISIBLE);
                break;
            case R.id.chat_send_pic:
                //动态申请权限
                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    //访问相册
                    InputPicture();
                }
                break;
            case R.id.chat_send_position:
                Intent intentPosition=new Intent(ChatActivity.this, LocationDetailActivity.class);
                intentPosition.putExtra("chatUser",chatUser);
                startActivity(intentPosition);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.speak:
                if(editLayout.getVisibility()!=View.GONE){
                    speakImg.setImageResource(R.drawable.icon_chat_keyboard);
                    recordButton.setVisibility(View.VISIBLE);
                    editLayout.setVisibility(View.GONE);
                }else{
                    speakImg.setImageResource(R.drawable.icon_speak);
                    recordButton.setVisibility(View.GONE);
                    editLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.chat_clock:
                Intent intentClock=new Intent(ChatActivity.this, LocationClockActivity.class);
                intentClock.putExtra("chatUser",chatUser);
                startActivityForResult(intentClock,0x11);
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
        Intent a=getIntent();
        /**
         * 订单添加消息
         */
       TradeInfo trade= (TradeInfo) a.getSerializableExtra("tradeInfo");
        customerAttachment.setContent(JSON.toJSON(trade).toString());

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
    public void SendConfirmTrade(TradeInfo trade){
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
        customerAttachment.setContent(JSON.toJSON(trade).toString());
        customMessage.setConfig(config);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(customMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Msg a=new Msg(JSON.toJSON(trade).toString(),Msg.TYPE_SEND_CARD);
                msgList.add(a);
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
                            intent.putExtra("tradeInfo",tradeInfo);
                            intent.putExtra("flag",flag);
                            intent.putExtra("chatUser",chatUser);
                            intent.putExtra("chatCommodity", charCommodity);
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

                    adapter.setOnItemImgClickListener(new MsgAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int isConfirm = 1;
                            view.findViewById(R.id.left_pic);
                            Intent intent=new Intent(ChatActivity.this, TradeDetailActivity.class);
                            intent.putExtra("isConfirm",isConfirm);
                            intent.putExtra("chatUser",chatUser);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                            finish();
                        }
                    });
                    break;
                case 2:
                    Date a=commerce.getTime();
                    Date now = new Date();
                    Date otherTime = new Date(now.getTime() - 10 * 60 * 1000); // 早10分钟的时间
                     Date otherTime1 = new Date(now.getTime() + 10 * 60 * 1000); // 晚10分钟的时间
                    // 比较时间
                    if (a.before(otherTime1)&&a.after(otherTime)) {
                        System.out.println("can da ka");
                    }
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                InputPicture();
            } else {
                Toast.makeText(this, "你拒绝打开此权限，无法进行下一步操作！", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void InputPicture() {
        //Intent.ACTION_PICK 从数据中选择一个项目 (item)，将被选中的项目返回。
        //MediaStore.Images.Media.EXTERNAL_CONTENT_URI 获取外部的URI
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //参数一:对应的数据的URI 参数二:使用该函数表示要查找文件的MIME类型
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.e("TAG", "onActivityResult: " + picturePath);
            SendPic(picturePath,data);
        }
        if(requestCode==0x11&&null!=data)
        {
            String a= data.getStringExtra("location");
            if(a!=null&&"1".equals(a))
            {
                System.out.println("successlocation");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result=CommerceOperate.setState(commerce.getCommerceid(),1,ChatActivity.this);
                        if(result==3)
                        {
                            MyToast.toast(ChatActivity.this,"交易已完成",3);
                        }
                        if(result==2)
                        {
                            MyToast.toast(ChatActivity.this,"等待对方确认交易",2);
                        }
                    }
                }).start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 发送图片
     * @Author: 李柏睿
     * @Time: 2023/5/2
     */
    public void SendPic(String filepath,Intent data){
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
        // 示例图片，需要开发者在相应目录下有图片
        File file = new File(filepath);
        // 创建一个图片消息
        IMMessage message = MessageBuilder.createImageMessage(account, sessionType, file, file.getName());
        message.setConfig(config);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
//                message.getAttachment();
                ImageAttachment msgAttachment=(ImageAttachment)message.getAttachment();
                String uri=msgAttachment.getThumbUrl();
                Log.e("TAG", "uri: " + uri);
                Log.e("TAG", "data: " + data.getData().toString());
                Msg customMsg = new Msg(filepath,Msg.TYPE_SEND_PIC);
                msgList.add(customMsg);
                MyToast.toast(ChatActivity.this,"图片信息发送成功",3);
            }

            @Override
            public void onFailed(int code) {
                MyToast.toast(ChatActivity.this,"图片信息发送失败",1);
            }
            @Override
            public void onException(Throwable exception) {
                MyToast.toast(ChatActivity.this,"图片信息发送异常",1);
            }
        });

    }

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    /**
     * 发送语音消息
     * @Author: 李柏睿
     * @Time: 2023/5/3 1:18
     */
    public void sendAudioMessage(String audioPath, int time){

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
        // 示例音频，需要开发者在相应目录下有文件
        File audioFile = new File(audioPath);
        // 音频时长，时间为示例
        long audioLength = time;
        // 创建音频消息
        IMMessage audioMessage = MessageBuilder.createAudioMessage(account, sessionType, audioFile, audioLength);

        audioMessage.setConfig(config);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(audioMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
//                message.getAttachment();
                AudioAttachment audioAttachment = (AudioAttachment)audioMessage.getAttachment();
                String uri=audioAttachment.getUrl();
                Log.e("TAG", "uri: " + uri);
                Msg customMsg = new Msg(audioPath+",and time is"+time,Msg.TYPE_SEND_AUDIO);
                msgList.add(customMsg);
                MyToast.toast(ChatActivity.this,"语音信息发送成功",3);
            }

            @Override
            public void onFailed(int code) {
                MyToast.toast(ChatActivity.this,"语音信息发送失败",1);
            }
            @Override
            public void onException(Throwable exception) {
                MyToast.toast(ChatActivity.this,"语音信息发送异常",1);
            }
        });
    }


    /**
     * 发送地理位置
     * @Author: 李柏睿
     * @Time: 2023/5/4
     */
    public void SendPosition(double lat,double lng,String poiName){
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
//        // 纬度
//        double lat = 30.3;
//        // 经度
//        double lng = 120.2;
        // 地理位置描述信息
        String addr = "杭州";
        // 创建地理位置信息
        IMMessage message = MessageBuilder.createLocationMessage(account, sessionType, lat, lng, poiName);
        // 创建一个图片消息
        message.setConfig(config);
        // 发送给对方
        NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
//                message.getAttachment();
                LocationAttachment msgAttachment=(LocationAttachment)message.getAttachment();
                String poi=msgAttachment.getAddress();
                Msg customMsg = new Msg(msgAttachment.getLongitude()+","+ msgAttachment.getLatitude()+"and"+poi,Msg.TYPE_SEND_LOCATION);
                msgList.add(customMsg);
                MyToast.toast(ChatActivity.this,"地理信息发送成功",3);
            }

            @Override
            public void onFailed(int code) {
                MyToast.toast(ChatActivity.this,"地理信息发送失败",1);
            }
            @Override
            public void onException(Throwable exception) {
                MyToast.toast(ChatActivity.this,"地理信息发送异常",1);
            }
        });

    }

}