package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cugerhuo.R;

public class XuanShangActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_xuan_shang);

        initView();
        ll_tab_one.setOnClickListener(this::onClickErhuo);
        ll_tab_four.setOnClickListener(this::onClickMessage);
        ll_tab_three.setOnClickListener(this::onClickPost);
        ll_tab_five.setOnClickListener(this::onClickMyCenter);

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
     * 点击消息图标，进行跳转到消息界面， overridePendingTransition(0, 0):去掉进场动画
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickMessage(View view){
        Intent i = null;
        i=new Intent(getApplicationContext(), MessageActivity.class);
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
}