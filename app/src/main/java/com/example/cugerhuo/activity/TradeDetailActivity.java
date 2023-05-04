package com.example.cugerhuo.activity;

import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commerce.Commerce;
import com.example.cugerhuo.access.commerce.CommerceOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.tools.entity.TradeInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;

import java.io.File;
import java.text.ParseException;

/***
 * 查看订单详情页
 * @Author: 李柏睿
 * @Time: 2023/5/1 16:35
 */
public class TradeDetailActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 消息发送函数
     */
    private final MyHandler MyHandler =new MyHandler(Looper.getMainLooper());
    /**返回上一级*/
    private ImageView returnImgTrade;
    /**商品图片*/
    private RoundedImageView tradeDetailGoodImg;
    /**商品信息*/
    private TextView tradeDetailGoodText;
    /**商品金额*/
    private TextView tradeDetailGoodPrice;
    /**交易金额时间地点展示*/
    private TextView tradeDetailPrice;
    private TextView tradeDetailTime;
    private TextView tradeDetailPosition;
    private TradeInfo tradeInfo;
    /**确认订单*/
    private Button confirmTrade;
    /**
     * 商品
     */
    private Commodity commodity;
    /**
     * 买家id
     */
    private int buyerId;
    /**
     * 聊天对象
     */
    private PartUserInfo chatUser=new PartUserInfo();
    /**订单id*/
    private int tradeId;
    /**是否已确认*/
    private int isConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);
        initView();
        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser= (PartUserInfo) intent.getSerializableExtra("chatUser");
        tradeInfo=(TradeInfo) intent.getSerializableExtra("tradeInfo");
        if(intent.getSerializableExtra("isConfirm")!=null){
            isConfirm = (int) intent.getSerializableExtra("isConfirm");
            commodity= (Commodity) intent.getSerializableExtra("chatCommodity");
            if(isConfirm==1){
                confirmTrade.setVisibility(View.GONE);

            }
        }
        initData();

    }
/**
 * 初始化数据
 */
public void initData()
{
    if(tradeInfo!=null)
    {
        tradeDetailTime.setText(tradeInfo.getTradeTime());
        tradeDetailPrice.setText(String.valueOf(tradeInfo.getPrice()));
        tradeDetailPosition.setText(tradeInfo.getTradePlace());
}   if(commodity!=null){
    if(!"".equals(commodity.getUrl1()))
        {String url1=commodity.getUrl1();
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
            String newUrl1 = getSandBoxPath(TradeDetailActivity.this) + url1;
            System.out.println("imager2"+url1);
            File f = new File(newUrl1);
            if (f.exists())
            {
                tradeDetailGoodImg.setImageURI(Uri.fromFile(f));
            }

        }  tradeDetailGoodPrice.setText(String.valueOf(commodity.getPrice()));
    tradeDetailGoodText.setText(commodity.getDescription());}}
    /**
     * 找到各控件
     * @Author: 李柏睿
     * @Time: 2023/5/2 0:13
     */
    public void initView(){
        returnImgTrade = findViewById(R.id.return_img);
        returnImgTrade.setOnClickListener(this);
        tradeDetailGoodImg = findViewById(R.id.trade_detail_image);
        tradeDetailGoodText = findViewById(R.id.trade_detail_good_tittle);
        tradeDetailGoodPrice = findViewById(R.id.trade_detail_good_price);
        tradeDetailPrice = findViewById(R.id.trade_detail_price);
        tradeDetailTime = findViewById(R.id.trade_detail_time);
        tradeDetailPosition = findViewById(R.id.trade_detail_position);
        confirmTrade = findViewById(R.id.trade_detail_confirm);
        confirmTrade.setOnClickListener(this);
    }

    /**
     * 点击事件函数
     * @Author: 李柏睿
     * @Time: 2023/5/2 0:18
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            /**
             * 返回上一级
             * @Author: 李柏睿
             * @Time: 2023/5/2 0:18
             */
            case R.id.return_img:
                finish();
                break;
            /**
             * 确认订单,插入mysql订单信息
             * @Author: 李柏睿
             * @Time: 2023/5/2 0:18
             */
            case R.id.trade_detail_confirm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Commerce a=new Commerce();
                        a.setSellerid(UserInfo.getid());
                        a.setCommodityid(132);
                        String price=tradeDetailPrice.getText().toString().replace("¥","");
                        a.setPrice(Double.valueOf(price));
                        a.setPlace(tradeDetailPosition.getText().toString());
                        a.setState(1);
                        a.setBuyerid(chatUser.getId());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
                        try {
                            a.setTime( formatter.parse(tradeDetailTime.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            CommerceOperate.insertCommerce(a,TradeDetailActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Message msg =new Message();
                        msg.arg1=1;
                        MyHandler.sendMessage(msg);
                    }
                }).start();
                break;
            default:
                break;
        }
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
            switch (msg.arg1)
            {
                case 1:
                    int confirmTrade = 1;
                    Intent intent=new Intent(TradeDetailActivity.this, ChatActivity.class);
                    intent.putExtra("confirmTrade",confirmTrade);
                    /**订单id*/
                    int tradeId = 1;
                    intent.putExtra("tradeId",tradeId);
                    TradeInfo a=new TradeInfo();
                    a.setPrice(Double.parseDouble(tradeDetailPrice.getText().toString().replace("¥","")));
                    a.setTradePlace(tradeDetailPosition.getText().toString());
                    a.setTradeTime(tradeDetailTime.getText().toString());
                    intent.putExtra("tradeInfo",a);
                    intent.putExtra("chatUser",chatUser);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                case 2:
                    break;
                default :
                    break;
            }
        }
    }
}