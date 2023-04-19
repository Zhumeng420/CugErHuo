package com.example.cugerhuo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.imessage.MessageActivity;
import com.example.cugerhuo.activity.post.PostBuyActivity;
import com.example.cugerhuo.activity.post.PostSellActivity;

/**
 * 发布选择
 * @Author: 李柏睿
 * @Time: 2023/3/22 15:40
 */
public class PublishSelectionActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_tab_three;
    private AlphaAnimation alphaAniShow, alphaAniHide;
    private LinearLayout AlphaShow;
    //跳转买卖
    private LinearLayout postBuy,postSell;
    private TranslateAnimation translateAniShow, translateAniHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_selection);
        initView();
        iv_tab_three.setOnClickListener(this::onClickExit);
    }

    public void initView(){
        postBuy = findViewById(R.id.publish_select_buy);
        postBuy.setOnClickListener(this);
        postSell = findViewById(R.id.publish_select_sell);
        postSell.setOnClickListener(this);
        iv_tab_three = (ImageView) findViewById(R.id.iv_tab_three);
        AlphaShow = (LinearLayout) findViewById(R.id.publish_select);
//        aa.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_2))
        iv_tab_three.setRotation(45);
        translateAnimation();
        AlphaShow.startAnimation(translateAniShow);
        AlphaShow.setVisibility(View.VISIBLE);

        //渐变效果
//        alphaAnimation();
//        AlphaShow.startAnimation(alphaAniShow);
//        AlphaShow.setVisibility(View.VISIBLE);
        //旋转动画
//        final RotateAnimation animation = new RotateAnimation(0.0f, 45.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration( 50 );
//        aa.startAnimation( animation );
    }

    /**
     * 买卖点击事件跳转
     * @Author: 李柏睿
     * @Time: 2023/4/13 17:12
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /**
             * 点击卖东西事件跳转
             * @Author: 李柏睿
             * @Time: 2023/4/13 17:13
             */
            case R.id.publish_select_buy:
                startActivity(new Intent(getApplicationContext(), PostBuyActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击买东西事件跳转
             * @Author: 李柏睿
             * @Time: 2023/4/13 17:14
             */
            case R.id.publish_select_sell:
                startActivity(new Intent(getApplicationContext(), PostSellActivity.class));
                overridePendingTransition(0,0);
                break;
            default:
                break;
        }
    }

    /**
     * 透明度过度动画
     * @Author: 李柏睿
     * @Time: 2023/3/22 13:41
     */
    private void alphaAnimation() {
        //显示
        //百分比透明度，从0%到100%显示
        alphaAniShow = new AlphaAnimation(0, 1);
        alphaAniShow.setDuration(800);
        //隐藏
        alphaAniHide = new AlphaAnimation(1, 0);
        alphaAniHide.setDuration(1000);
    }

    /**
     * 上下位移动画
     * @Author: 李柏睿
     * @Time: 2023/3/22 13:47
     */
    private void translateAnimation() {

        //向上位移显示动画  从自身位置的最下端向上滑动了自身的高度
        translateAniShow = new TranslateAnimation(
                //RELATIVE_TO_SELF表示操作自身,fromXValue表示开始的X轴位置,fromXValue表示结束的X轴位置,fromXValue表示开始的Y轴位置,fromXValue表示结束的Y轴位置
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                1,
                Animation.RELATIVE_TO_SELF,
                0);
        translateAniShow.setRepeatMode(Animation.REVERSE);
        translateAniShow.setDuration(200);

        //向下位移隐藏动画  从自身位置的最上端向下滑动了自身的高度
        translateAniHide = new TranslateAnimation(
                //RELATIVE_TO_SELF表示操作自身,fromXValue表示开始的X轴位置,fromXValue表示结束的X轴位置,fromXValue表示开始的Y轴位置,fromXValue表示结束的Y轴位置
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                1);
        translateAniHide.setRepeatMode(Animation.REVERSE);
        translateAniHide.setDuration(200);
    }

    /**
     * 点击返回上一个界面 + 退出动画
     * @Author: 李柏睿
     * @Time: 2023/3/22 16:45
     */
    public void onClickExit(View view){
        AlphaShow.startAnimation(translateAniHide);
        translateAniHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                final RotateAnimation animationx = new RotateAnimation(0.0f, -45.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animationx.setDuration( 500 );
                iv_tab_three.startAnimation( animationx );
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaShow.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(0,0);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }



}