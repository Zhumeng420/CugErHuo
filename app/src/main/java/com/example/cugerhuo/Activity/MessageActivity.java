package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cugerhuo.R;

/**
 * 消息主页
 * 实时通信：
 * @link https://yunxin.163.com/m/im?from=bd3mjjim20220714
 * @author 朱萌
 */
public class MessageActivity extends AppCompatActivity {


    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author: 唐小莉
     * @time 2023/3/20 16:36
     */

    private ImageView iv_tab_one;
    private ImageView iv_tab_two;
    private ImageView iv_tab_three;
    private ImageView iv_tab_four;
    private ImageView iv_tab_five;


    private LinearLayout ll_tab_one;
    private LinearLayout ll_tab_two;
    private LinearLayout ll_tab_three;
    private LinearLayout ll_tab_four;
    private LinearLayout ll_tab_five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initView();
        ll_tab_one.setOnClickListener(this::onClickErhuo);
        ll_tab_two.setOnClickListener(this::onClickXuanShang);
        ll_tab_three.setOnClickListener(this::onClickPost);
        ll_tab_five.setOnClickListener(this::onClickMyCenter);
        iv_tab_three = (ImageView) findViewById(R.id.iv_tab_three);
        iv_tab_three.setOnClickListener(this::onClickPublish);

    }

    /**
     * 初始化各个控件，找到对应的组件
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void initView(){
        ll_tab_one=findViewById(R.id.ll_tab_one);
        ll_tab_two=findViewById(R.id.ll_tab_two);
        ll_tab_three=findViewById(R.id.ll_tab_three);
        ll_tab_four=findViewById(R.id.ll_tab_four);
        ll_tab_five=findViewById(R.id.ll_tab_five);
    }

    /**
     * 点击二货图标，进行跳转到主界面， overridePendingTransition(0, 0):去掉进场动画，使用finish对这个界面进行进程销毁
     * 以便后续返回直接跳转至首页
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickErhuo(View view){
        Intent i = null;

        i=new Intent(getApplicationContext(), ErHuoActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }
    /**
     * 点击悬赏图标，进行跳转到悬赏界面， overridePendingTransition(0, 0):去掉进场动画
     * 使用finish对这个界面进行进程销毁以便后续返回直接跳转至首页
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickXuanShang(View view){
        Intent i = null;
        i=new Intent(getApplicationContext(), XuanShangActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }
    /**
     * 点击发布图标，进行跳转到发布界面， overridePendingTransition(0, 0):去掉进场动画
     * 使用finish对这个界面进行进程销毁以便后续返回直接跳转至首页
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickPost(View view){

    }
    /**
     * 点击个人中心图标，进行跳转到个人中心界面， overridePendingTransition(0, 0):去掉进场动画
     * 使用finish对这个界面进行进程销毁以便后续返回直接跳转至首页
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:32
     */
    public void onClickMyCenter(View view){
        Intent i = null;
        i=new Intent(getApplicationContext(), MyCenterActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }

    /**
     * 重写finish方法，去掉出场动画
     * @author 唐小莉
     * @Time 2023/3/20 16:31
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 点击中间加号按钮跳转选择界面+跳转动画
     * @param view
     * @Author: 李柏睿
     * @Time: 2023/3/22 16:38
     */
    public void onClickPublish(View view) {
        final RotateAnimation animation = new RotateAnimation(0.0f, 90.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration( 500 );
        iv_tab_three.startAnimation( animation );
        Intent intent = new Intent(getApplicationContext(),PublishSelectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }


}