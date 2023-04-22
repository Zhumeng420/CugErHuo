package com.example.cugerhuo.activity;

import static com.example.cugerhuo.activity.MyCenterActivity.focusNum;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.adapter.ViewPagerAdapter;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.Fragment.MyFragment;
import com.example.cugerhuo.views.MyScrollView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;

/**
 * 查看他人的个人中心
 * @Author: 李柏睿
 * @Time: 2023/3/27 14:40
 */

public class OtherPeopleActivity extends AppCompatActivity {

    /**
     *滚动
     */
    private MyScrollView scrollView;
    /**
     * 背景图的顶部
     */
    private LinearLayout llSVTitle;
    /**
     * 在下滑过程慢慢浮现的顶部
     */
    private LinearLayout llSVSmallTitle;
    /**
     * 下面的fragment导航栏(进行吸顶)
     */
    private TabLayout move,stop;
    /**
     * llSVTitle的高度
     */
    private int mTitleHeight;
    /**
     * llSVSmallTitle的高度
     */
    private int mSmallTitleHeight;
    /**
     * 导航栏高度，距离顶部距离，(大)头像高度，(小)弹出按钮高度
     */
    private int moveHeight,moveTop,imgHeight,popHeight;
    /**
     * 是否关注按钮,弹出关注按钮
     */
    private Button isFollowed,popFollowed;
    /**
     * 是否关注按钮在的LinearLayout
     */
    LinearLayout allBottom;
    /**
     * 是否关注伴随改变
     */
    private ImageView isF;
    ViewPager viewPager;
    ArrayList<MyFragment> fragments;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;
    /**
     * 下拉弹出的小头像，用户头像
     */
    ImageView popImg,userImg;
    private AlphaAnimation alphaAniShow, alphaAniHide;
    private TranslateAnimation translateAniShow,translateAniHide;
    /**
     * 用户名
     */
    private TextView username;
    /**
     * 用户关注数
     */
    private TextView userConcernNum;
    /**
     * 用户粉丝数
     */
    private TextView userFansNum;
    /**
     * 用户自我介绍
     */
    private TextView introduction;

    private boolean isConcern;

    /**
     * 用户信息类
     */
    private  PartUserInfo partUserInfo=new PartUserInfo();
    private final MyHandler MyHandler =new MyHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_people);
        init();
        initFragment();
        initIntendInfo();

        Intent intent = new Intent();
        intent.putExtra("isConcern",isConcern);
        System.out.println("xinaccchhhhhhhhaaaaa"+isConcern);
        setResult(1,intent);
        /**
         *  给viewPager重置高度
         */
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
        isFollowed.setOnClickListener(this::onClickFollowed);
        popFollowed.setOnClickListener(this::onClickFollowed);
        isF.setOnClickListener(this::onClickFollowedPop);

        // 5、开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            int focusNum;
            /**
             * 获取关注数量
             */
            focusNum=UserOperate.getFocusNum(partUserInfo.getId(),OtherPeopleActivity.this);

            msg.arg2=focusNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
        }).start();
        // 5、开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 2;
            int fansNum;
            /**
             * 获取粉丝数量
             */
            fansNum=UserOperate.getFansNum(partUserInfo.getId(),OtherPeopleActivity.this);
            msg.arg2=fansNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
        }).start();

    }

    /**
     * 初始化各控件
     * @author 唐小莉
     * @time 2023/4/12
     */
    public void init(){
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
        /**
         * 弹出关注按钮所在的LinearLayout
         */
        allBottom = findViewById(R.id.all_bott);
        username=findViewById(R.id.username);
        userConcernNum=findViewById(R.id.user_concern);
        userFansNum=findViewById(R.id.user_fans);
        introduction=findViewById(R.id.introduction);

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
        /**设置初始fragment*/
        viewPager.setCurrentItem(0,false);


        /**关联ViewPager*/
        move.setupWithViewPager(viewPager);
        stop.setupWithViewPager(viewPager);
        /**设置固定的tab*/
        move.setTabMode(TabLayout.MODE_FIXED);
        stop.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 获取上个页面传递过来的信息
     * @author 唐小莉
     * @time 2023/4/19
     */
    public void initIntendInfo(){
        /**
         * 直接获取上个界面传递的用户信息
         */
        Intent intent =getIntent();
        partUserInfo= (PartUserInfo) intent.getSerializableExtra("concernUser");
        isConcern=partUserInfo.getConcern();
        /**
         * 将用户信息进行加载显示
         */
        username.setText(partUserInfo.getUserName());
        introduction.setText(partUserInfo.getSignature());
        if (!"".equals(partUserInfo.getImageUrl())&&partUserInfo.getImageUrl()!=null)
        {
            userImg.setImageURI(Uri.fromFile(new File(partUserInfo.getImageUrl())));
            popImg.setImageURI(Uri.fromFile(new File(partUserInfo.getImageUrl())));
        }
        isFollowed(partUserInfo.getConcern());

    }

    public void onClickFollowed(View view){
        /**
         * 如果已经关注，点击进入聊天页面
         */
        if(partUserInfo.getConcern()){
            Intent intent=new Intent(OtherPeopleActivity.this, ChatActivity.class);
            startActivity(intent);
        }
        /**
         * 如果是没有关注，点击进行关注
         */
        else{
            new Thread(() -> {
                Message msg = Message.obtain();
                msg.arg1 = 3;
                UserOperate.setConcern(UserInfo.getid(),partUserInfo.getId(),getActivity());
                //isConcern=true;
                //4、发送消息
                MyHandler.sendMessage(msg);

            }).start();
        }

    }

    public void onClickFollowedPop(View view){
        /**
         * 如果没有关注，点击进入聊天页面
         */
        if(!partUserInfo.getConcern()){
            Intent intent=new Intent(OtherPeopleActivity.this, ChatActivity.class);
            startActivity(intent);
        }
        else{
            new Thread(() -> {
                Message msg = Message.obtain();
                msg.arg1 = 4;
                UserOperate.getIfDeleteConcern(UserInfo.getid(),partUserInfo.getId(),getActivity());
                //isConcern=true;
                //4、发送消息
                MyHandler.sendMessage(msg);

            }).start();
        }
    }

    /**
     * 上下位移动画
     * @Author: 李柏睿
     * @Time: 2023/3/22 13:47
     */
    private void translateAnimation() {
        /** 向上位移显示动画  从自身位置的最下端向上滑动了自身的高度*/
        translateAniShow = new TranslateAnimation(
                /** RELATIVE_TO_SELF表示操作自身*/
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示开始的X轴位置*/
                0,
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示结束的X轴位置*/
                0,
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示开始的Y轴位置*/
                1,
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示结束的Y轴位置*/
                0);
        translateAniShow.setRepeatMode(Animation.REVERSE);
        translateAniShow.setDuration(100);
        /** 向下位移隐藏动画  从自身位置的最上端向下滑动了自身的高度*/
        translateAniHide = new TranslateAnimation(
                /** RELATIVE_TO_SELF表示操作自身*/
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示开始的X轴位置*/
                0,
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示结束的X轴位置*/
                0,
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示开始的Y轴位置*/
                0,
                Animation.RELATIVE_TO_SELF,
                /** fromXValue表示结束的Y轴位置*/
                1);
        translateAniHide.setRepeatMode(Animation.REVERSE);
        translateAniHide.setDuration(200);
    }
    /**
     * 消息发送接收，异步更新UI
     * @author 施立豪
     * @time 2023/3/26
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                /**
                 * 更新关注数
                 */
                case 1:
                    userConcernNum.setText(String.valueOf(msg.arg2));
                    break;
                /**
                 * 更新粉丝数
                 * @time 2023/3/27
                 */
                case 2:
                    userFansNum.setText(String.valueOf(msg.arg2));
                    break;
                /**
                 * 点击关注按钮
                 */
                case 3:
                    isFollowed(true);
                    isConcern=true;
                    partUserInfo.setConcern(true);
                    Intent intent = new Intent();
                    intent.putExtra("isConcern",isConcern);
                    System.out.println("xinaccchhhhhhhhaaaaa"+isConcern);
                    setResult(1,intent);
                    System.out.println("guanzhuuuanzhu");
                    focusNum++;
                    break;
                case 4:
                    isFollowed(false);
                    isConcern=false;
                    partUserInfo.setConcern(false);
                    Intent intent1 = new Intent();
                    intent1.putExtra("isConcern",isConcern);
                    System.out.println("xinaccchhhhhhhhaaaaa"+isConcern);
                    setResult(1,intent1);
                    focusNum--;
                    break;
                default:
                    break;
            }
        }
    }


}