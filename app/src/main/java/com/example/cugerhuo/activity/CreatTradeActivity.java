package com.example.cugerhuo.activity;

import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.tools.entity.TradeInfo;
import com.example.cugerhuo.views.EditDialog;
import com.example.cugerhuo.views.KeyboardUtil;
import com.example.cugerhuo.views.MyKeyBoardView;
import com.github.gzuliyujiang.wheelpicker.DatimePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatimePickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity;
import com.github.gzuliyujiang.wheelpicker.widget.DatimeWheelLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

/***
 * 生成订单界面
 * @Author: 李柏睿
 * @Time: 2023/4/30 19:43
 */
public class CreatTradeActivity extends AppCompatActivity implements View.OnClickListener{

    /**返回图片*/
    private ImageView returnImg;
    /**交易金额时间地点三个线性布局*/
    private LinearLayout priceLayout,timeLayout,positionLayout;
    /**交易金额时间地点三个结果显示*/
    private TextView priceText,timeText,positionText,goodTitle,goodPrice;
    private RoundedImageView goodImage;
    /**数字键盘*/
    LinearLayout ll_price;
    EditText et_price;
    MyKeyBoardView keyboard_view;
    LinearLayout ll_price_select;
    View blankClick;
    /**生成订单*/
    private Button createTrade;

    /**
     * 聊天对象
     */
    private PartUserInfo chatUser=new PartUserInfo();
    private Commodity charCommodity=new Commodity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_trade);
        /**
         * 从上个界面获取聊天对象信息
         */
        Intent intent =getIntent();
        chatUser= (PartUserInfo) intent.getSerializableExtra("chatUser");
        charCommodity=(Commodity) intent.getSerializableExtra("chatCommodity");
        initView();


    }

    /**
     * 初始化各个控件
     * @Author: 李柏睿
     * @Time: 2023/4/30 20:28
     */
    public void initView(){
        returnImg = findViewById(R.id.return_img);
        returnImg.setOnClickListener(this);
        priceLayout = findViewById(R.id.create_trade_price_layout);
        priceLayout.setOnClickListener(this);
        timeLayout = findViewById(R.id.create_trade_time_layout);
        timeLayout.setOnClickListener(this);
        positionLayout = findViewById(R.id.create_trade_position_layout);
        positionLayout.setOnClickListener(this);
        priceText = findViewById(R.id.create_trade_price);
        timeText = findViewById(R.id.create_trade_time);
        positionText = findViewById(R.id.create_trade_position);

        ll_price = findViewById(R.id.ll_price);
        et_price = findViewById(R.id.et_price);
        keyboard_view = findViewById(R.id.keyboard_view);
        ll_price_select = findViewById(R.id.ll_price_select);
        blankClick = findViewById(R.id.click_blank);
        blankClick.setOnClickListener(this);
        createTrade = findViewById(R.id.create_trade);
        createTrade.setOnClickListener(this);

        goodTitle=findViewById(R.id.trade_good_tittle);
        goodPrice=findViewById(R.id.trade_good_price);
        goodImage=findViewById(R.id.trade_image);
        /**
         * 初始化商品UI
         */
        if(charCommodity!=null)
        {   priceText.setText("¥"+charCommodity.getPrice().toString());
            positionText.setText("");
            goodTitle.setText(charCommodity.getDescription());
            goodPrice.setText("¥"+charCommodity.getPrice().toString());
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
                String newUrl1 = getSandBoxPath(CreatTradeActivity.this) + url1;
                System.out.println("imager2"+url1);
                File f = new File(newUrl1);
                if (f.exists())
                {
                    goodImage.setImageURI(Uri.fromFile(f));
                }
            }
        }

    }

    /**
     * 点击事件函数
     * @Author: 李柏睿
     * @Time: 2023/4/30
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            /**
             * 返回上一级
             * @Author: 李柏睿
             * @Time: 2023/4/30
             */
            case R.id.return_img:
                finish();
                break;
            /**
             * 键盘收起
             * @Author: 李柏睿
             * @Time: 2023/4/30
             */
            case R.id.click_blank:
                ll_price_select.setVisibility(View.GONE);
                break;
            /**
             * 修改交易金额
             * @Author: 李柏睿
             * @Time: 2023/4/30
             */
            case R.id.create_trade_price_layout:
                /**数字键盘*/
                final KeyboardUtil keyboardUtil = new KeyboardUtil(CreatTradeActivity.this);
                keyboardUtil.setOnOkClick(new KeyboardUtil.OnOkClick() {
                    @Override
                    public void onOkClick() {
                        if (validate()) {
                            ll_price_select.setVisibility(View.GONE);
                            priceText.setText("¥"+et_price.getText() );
                        }
                    }
                });
                keyboardUtil.setOnCancelClick(new KeyboardUtil.onCancelClick() {
                    @Override
                    public void onCancellClick() {
                        ll_price_select.setVisibility(View.GONE);
                    }
                });
                keyboardUtil.attachTo(et_price);
                et_price.setFocusable(true);
                et_price.setFocusableInTouchMode(true);
                et_price.requestFocus();
                ll_price_select.setVisibility(View.VISIBLE);
                et_price.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        keyboardUtil.attachTo(et_price);
                        return false;
                    }
                });


                break;
            /**
             * 修改交易时间
             * @Author: 李柏睿
             * @Time: 2023/4/30
             */
            case R.id.create_trade_time_layout:

                DatimePicker picker = new DatimePicker(CreatTradeActivity.this);
                final DatimeWheelLayout wheelLayout = picker.getWheelLayout();
                picker.setOnDatimePickedListener(new OnDatimePickedListener() {
                    @Override
                    public void onDatimePicked(int year, int month, int day, int hour, int minute, int second) {
                        String text = year + "-" + month + "-" + day + " " + hour + ":" + (minute>10? minute : "0"+minute) + ":" + (second>10? second : "0"+second);
                        timeText.setText(text);
                    }
                });
                wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
                wheelLayout.setTimeMode(TimeMode.HOUR_24_NO_SECOND);
                wheelLayout.setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(10));
                wheelLayout.setDateLabel("年", "月", "日");
                wheelLayout.setTimeLabel("时", "分", "秒");
                picker.show();

                break;
            /**
             * 修改交易地点
             * @Author: 李柏睿
             * @Time: 2023/4/30
             */
            case R.id.create_trade_position_layout:
                EditDialog editDialog=new EditDialog(getActivity());
                /**
                 * 确定按钮回调
                 */
                editDialog.setConfirmListener(new EditDialog.ConfirmListener() {
                    @Override
                    public void onConfirmClick() {
                        positionText.setText(editDialog.getEdit());
                    }
                });
                editDialog.show();
                break;
            case R.id.create_trade:
                if(positionText.getText()!=null&&!"".equals(positionText.getText())){
                Intent intent=new Intent(CreatTradeActivity.this, ChatActivity.class);
                TradeInfo tradeInfo=new TradeInfo();
                tradeInfo.setPrice(Double.valueOf(priceText.getText().toString().replace("¥","")));
                tradeInfo.setTradeTime(timeText.getText().toString());
                tradeInfo.setTradePlace(positionText.getText().toString());
                intent.putExtra("chatUser",chatUser);
                Commodity e= (Commodity) getIntent().getSerializableExtra("chatCommodity");
                intent.putExtra("chatCommodity",e);
                int iWant = 1;
                intent.putExtra("iWant",iWant);
                /**是否生成订单*/
                int creatTrade = 1;
                intent.putExtra("isTrade",creatTrade);
                /**订单id*/
                int tradeId = 1;
                intent.putExtra("tradeId",tradeId);
                intent.putExtra("tradeInfo",tradeInfo);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();}else
                {
                    MyToast.toast(CreatTradeActivity.this,"请填写交易地点",0);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 是否输入判断并弹窗提醒
     * @author: 李柏睿
     * @time: 2023/4/30
     */
    public boolean validate() {
        if ("".equals(et_price.getText().toString())) {
            Toast.makeText(getApplication(), "交易金额不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 键盘收起放下
     * @Author: 李柏睿
     * @Time: 2023/4/30
     */
    @Override
    public void onBackPressed() {
        if (ll_price_select.getVisibility() == View.VISIBLE) {
            ll_price_select.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}