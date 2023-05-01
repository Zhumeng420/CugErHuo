package com.example.cugerhuo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.makeramen.roundedimageview.RoundedImageView;

/***
 * 查看订单详情页
 * @Author: 李柏睿
 * @Time: 2023/5/1 16:35
 */
public class TradeDetailActivity extends AppCompatActivity implements View.OnClickListener{

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
    /**确认订单*/
    private Button confirmTrade;
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
        if(intent.getSerializableExtra("isConfirm")!=null){
            isConfirm = (int) intent.getSerializableExtra("isConfirm");
            if(isConfirm==1){
                confirmTrade.setVisibility(View.GONE);
            }
        }

    }

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
             * 确认订单
             * @Author: 李柏睿
             * @Time: 2023/5/2 0:18
             */
            case R.id.trade_detail_confirm:
                int confirmTrade = 1;
                Intent intent=new Intent(TradeDetailActivity.this, ChatActivity.class);
                intent.putExtra("confirmTrade",confirmTrade);
                /**订单id*/
                int tradeId = 1;
                intent.putExtra("tradeId",tradeId);
                intent.putExtra("chatUser",chatUser);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            default:
                break;
        }
    }
}