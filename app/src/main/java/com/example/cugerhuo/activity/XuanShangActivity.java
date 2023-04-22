package com.example.cugerhuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.XuanShangInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewXuanShangAdapter;
import com.example.cugerhuo.activity.imessage.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class XuanShangActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author: 唐小莉
     * @time 2023/3/20 16:36
     */
    private ImageView iv_tab_three;
    private LinearLayout ll_tab_one;
    private LinearLayout ll_tab_two;
    private LinearLayout ll_tab_three;
    private LinearLayout ll_tab_four;
    private LinearLayout ll_tab_five;
    private RecyclerView recyclerView;
    private List<XuanShangInfo> test=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_shang);
        initView();
    }

    /**
     * 初始化各个控件，找到对应的组件
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void initView(){
        ll_tab_one=findViewById(R.id.ll_tab_one);
        ll_tab_one.setOnClickListener(this);
        ll_tab_two=findViewById(R.id.ll_tab_two);
        ll_tab_two.setOnClickListener(this);
        ll_tab_four=findViewById(R.id.ll_tab_four);
        ll_tab_four.setOnClickListener(this);
        ll_tab_five=findViewById(R.id.ll_tab_five);
        ll_tab_five.setOnClickListener(this);
        iv_tab_three = (ImageView) findViewById(R.id.iv_tab_three);
        iv_tab_three.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView_xuanhsang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(int i=0;i<12;i++){
            XuanShangInfo part= new XuanShangInfo();
            test.add(part);
        }

        RecyclerViewXuanShangAdapter adapter = new RecyclerViewXuanShangAdapter(XuanShangActivity.this,test);
        recyclerView.setAdapter(adapter);



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
     * 底部导航栏点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_tab_one:
                startActivity(new Intent(getApplicationContext(), ErHuoActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            /**
             * 点击悬赏图标，进行跳转到悬赏界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_two:
                break;
            /**
             * 点击中间加号按钮跳转选择界面+跳转动画
             * @Author: 李柏睿
             * @Time: 2023/3/22 16:38
             */
            case R.id.iv_tab_three:
                final RotateAnimation animation = new RotateAnimation(0.0f, 90.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration( 500 );
                iv_tab_three.startAnimation( animation );
                startActivity(new Intent(getApplicationContext(),PublishSelectionActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击消息图标，进行跳转到消息界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_four:
                startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            /**
             * 点击个人中心图标，进行跳转到个人中心界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_five:
                startActivity(new Intent(getApplicationContext(), MyCenterActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            default:
                break;
        }

    }



}