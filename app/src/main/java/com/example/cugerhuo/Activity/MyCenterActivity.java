package com.example.cugerhuo.Activity;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;
import static java.security.AccessController.getContext;

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

/**
 * 指纹验证：
 * @link https://www.bilibili.com/video/BV1664y1d7oG/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author  朱萌
 */
public class MyCenterActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_my_center);
        initView();

        ll_tab_one.setOnClickListener(this::onClickErhuo);
        ll_tab_four.setOnClickListener(this::onClickMessage);
        ll_tab_three.setOnClickListener(this::onClickPost);
        ll_tab_two.setOnClickListener(this::onClickXuanShang);



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