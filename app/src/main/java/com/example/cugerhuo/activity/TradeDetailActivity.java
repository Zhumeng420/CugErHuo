package com.example.cugerhuo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cugerhuo.R;
import com.makeramen.roundedimageview.RoundedImageView;

/***
 * 查看订单详情页
 * @Author: 李柏睿
 * @Time: 2023/5/1 16:35
 */
public class TradeDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);
    }
}