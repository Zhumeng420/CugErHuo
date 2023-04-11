package com.example.cugerhuo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.adapter.ViewPagerAdapter;
import com.example.cugerhuo.fragment.MyFragment;
import com.example.cugerhuo.views.MyScrollView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * 查看他人的个人中心
 * @Author: 李柏睿
 * @Time: 2023/3/27 14:40
 */

public class OtherPeopleActivity extends AppCompatActivity {
    private MyScrollView scrollView;//滚动
    private LinearLayout llSVTitle;//背景图的顶部
    private LinearLayout llSVSmallTitle;//在下滑过程慢慢浮现的顶部
    private TabLayout move,stop;//下面的fragment导航栏(进行吸顶)
    private int mTitleHeight;//llSVTitle的高度
    private int mSmallTitleHeight;//llSVSmallTitle的高度
    private int moveHeight,moveTop,imgHeight,popHeight;//导航栏高度，距离顶部距离，(大)头像高度，(小)弹出按钮高度
    private Button isFollowed,popFollowed; //是否关注按钮,弹出关注按钮
    LinearLayout allBottom;//是否关注按钮在的LinearLayout
    private ImageView isF; //是否关注伴随改变
    ViewPager viewPager;
    ArrayList<MyFragment> fragments;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;
    ImageView popImg,userImg;//下拉弹出的小头像，用户头像
    private AlphaAnimation alphaAniShow, alphaAniHide;
    private TranslateAnimation translateAniShow,translateAniHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_people);
        scrollView = findViewById(R.id.scrollView_scrollview);
        llSVTitle = findViewById(R.id.ll_scrollview_title);
        llSVSmallTitle = findViewById(R.id.ll_scrollview_small_title);
        isFollowed = findViewById(R.id.is_followed_1);
        isF = findViewById(R.id.is_followed_2);
        move = findViewById(R.id.move);
        stop = findViewById(R.id.stop);
        viewPager = findViewById(R.id.viewPager);
        popImg = findViewById(R.id.user_img_pop);
        userImg = findViewById(R.id.other_user_img);
        popFollowed = findViewById(R.id.is_followed_pop);
        allBottom = findViewById(R.id.all_bott);//弹出关注按钮所在的LinearLayout
        initFragment();

        //调用fragment控件
//        Button button = getActivity().findViewById(R.id.zhan);

        /**给viewPager重置高度*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                // 切换到当前页面，重置高度
                viewPager.requestLayout();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        /**进行下滑操作后fragment导航栏的吸顶以及顶部固定*/
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                mTitleHeight = llSVTitle.getHeight();
                mSmallTitleHeight = llSVSmallTitle.getHeight();
                float cha = mTitleHeight - mSmallTitleHeight;//为正数
                moveHeight = move.getHeight();
                moveTop = move.getTop();
                imgHeight = userImg.getHeight()+mSmallTitleHeight;
                popHeight = allBottom.getTop();//关注按钮弹出具体高度

                /**顶部栏 置顶*/
                if (scrollY == 0) {
                    llSVSmallTitle.setVisibility(View.GONE);
                } else if (scrollY > 0) {
                    llSVSmallTitle.setVisibility(View.VISIBLE);
                }

                /**图片弹出动画*/
                if(scrollY >= imgHeight){
                    if(popImg.getVisibility()==View.INVISIBLE){
                        translateAnimation();
                        popImg.startAnimation(translateAniShow);
                        popImg.setVisibility(View.VISIBLE);
                    }
                }else if(scrollY < imgHeight){
                    if(popImg.getVisibility()==View.VISIBLE){
                        translateAnimation();
                        popImg.startAnimation(translateAniHide);
                        popImg.setVisibility(View.INVISIBLE);
                    }
                }

                /**关注按钮弹出动画*/
                if(scrollY >= popHeight){
                    if(popFollowed.getVisibility()==View.INVISIBLE){
                        translateAnimation();
                        popFollowed.startAnimation(translateAniShow);
                        popFollowed.setVisibility(View.VISIBLE);
                        popFollowed.setText(isFollowed.getText());
                    }
                }else if(scrollY < popHeight){
                    if(popFollowed.getVisibility()==View.VISIBLE){
                        translateAnimation();
                        popFollowed.startAnimation(translateAniHide);
                        popFollowed.setVisibility(View.INVISIBLE);
                    }
                }


                /**滑动到下面,吸顶*/
                if(scrollY >= moveTop - mSmallTitleHeight){
                    stop.setVisibility(View.VISIBLE);
                }else {  //反之隐藏
                    stop.setVisibility(View.GONE);
                }

                /**设置透明度渐变*/
                if (scrollY <= cha) {
                    float mAlpha = scrollY / cha * 3/2;
                    if (mAlpha < 0) {
                        mAlpha = 0;
                    } else if (mAlpha > 1) {
                        mAlpha = 1;
                    }
                    llSVSmallTitle.setAlpha(mAlpha);
                } else {
                    llSVSmallTitle.setAlpha(1);
                }
            }
        });
    }

    /**
     * 根据是否关注改变ui界面
     * @param f 是否关注
     * @Author: 李柏睿
     * @Time: 2023/3/27 22:46
     */
    public void isFollowed(boolean f){
        if(f){
            /** 已关注状态 */
            isFollowed.setText("发消息");
            isFollowed.setBackgroundResource(R.drawable.followed);
            popFollowed.setBackgroundResource(R.drawable.followed);
            isF.setImageResource(R.drawable.icon_followed);
        }else{
            /** 未关注状态 */
            isFollowed.setText("关注");
            isFollowed.setBackgroundResource(R.drawable.not_followed);
            popFollowed.setBackgroundResource(R.drawable.not_followed);
            isF.setImageResource(R.drawable.icon_chat);

        }
    }

    /**
     * fragment初始化
     * @Author: 李柏睿
     * @Time: 2023/3/28 11:28
     */
    public void initFragment(){
        /**初始化数据*/
        fragments = new ArrayList<>();
        fragments.add(new MyFragment("商品",null,R.layout.other_fragment_product));
        fragments.add(new MyFragment("评价",null,R.layout.other_fragment_appraise));

        /**设置ViewPager适配器*/
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);

        /**关联ViewPager*/
        move.setupWithViewPager(viewPager);
        stop.setupWithViewPager(viewPager);
        /**设置固定的tab*/
        move.setTabMode(TabLayout.MODE_FIXED);
        stop.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 上下位移动画
     * @Author: 李柏睿
     * @Time: 2023/3/22 13:47
     */
    private void translateAnimation() {
        //向上位移显示动画  从自身位置的最下端向上滑动了自身的高度
        translateAniShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
                0,//fromXValue表示开始的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示结束的X轴位置
                Animation.RELATIVE_TO_SELF,
                1,//fromXValue表示开始的Y轴位置
                Animation.RELATIVE_TO_SELF,
                0);//fromXValue表示结束的Y轴位置
        translateAniShow.setRepeatMode(Animation.REVERSE);
        translateAniShow.setDuration(100);
        //向下位移隐藏动画  从自身位置的最上端向下滑动了自身的高度
        translateAniHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
                0,//fromXValue表示开始的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示结束的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示开始的Y轴位置
                Animation.RELATIVE_TO_SELF,
                1);//fromXValue表示结束的Y轴位置
        translateAniHide.setRepeatMode(Animation.REVERSE);
        translateAniHide.setDuration(200);
    }
}