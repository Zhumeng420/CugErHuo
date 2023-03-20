package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cugerhuo.Activity.adapter.MyFragmentTabAdapter;
import com.example.cugerhuo.FastLogin.login.OneKeyLoginActivity;
import com.example.cugerhuo.Fragment.ErHuoFragment;
import com.example.cugerhuo.Fragment.MessageFragment;
import com.example.cugerhuo.Fragment.MyCenterFragment;
import com.example.cugerhuo.Fragment.XuanShangFragment;
import com.example.cugerhuo.Fragment.PostFragment;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部带悬浮球导航栏：
 * @link https://github.com/ittianyu/BottomNavigationViewEx
 * 使用Center Floating Action Button，分布对应ErHuoActivity、MessageActivity、MyCenterActivity、XuanShangActivity
 * @author 朱萌
 */

/**
 * 页面之间的过渡动画：
 * @link https://www.bilibili.com/video/BV1ha411K7MZ/?spm_id_from=333.337.search-card.all.click&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * 在res中的animation文件夹存放相关的转场xml文件
 * 包括slide_from_bottom,slide_from_left,slide_from_right,slide_from_top
 * slide_to_bottom,slide_from_to_left,slide_to_right,slide_to_top
 * zoom_in,zoom_out
 * static_animation
 * @author 朱萌
 *
 *----------------------------------------
 * 液体过渡动画（这个似乎更好看）
 * 使用github上的开源组件
 * @link https://www.bilibili.com/video/BV1mb4y197rJ/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @link https://www.bilibili.com/video/BV11r4y1P76D/?spm_id_from=333.788.recommend_more_video.-1&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 */

/**
 * item的加载动画
 * @link https://www.bilibili.com/video/BV1gT411v7gp/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author 朱萌
 */

/**
 * 下拉刷新组件
 * @link https://github.com/scwang90/SmartRefreshLayout
 * @author
 */

/**
 * "贰货“主页
 */
public class ErHuoActivity extends AppCompatActivity {


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

    private MyToast toast=new MyToast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo);
        initView();
        ll_tab_four.setOnClickListener(this::onClickMessage);
        ll_tab_two.setOnClickListener(this::onClickXuanShang);
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

    }

    /**
     * 点击悬赏图标，进行跳转到悬赏界面， overridePendingTransition(0, 0):去掉进场动画
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickXuanShang(View view){
        Intent i = null;
        i=new Intent(getApplicationContext(), XuanShangActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }
    /**
     * 点击发布图标，进行跳转到发布界面， overridePendingTransition(0, 0):去掉进场动画
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickPost(View view){

    }
    /**
     * 点击个人中心图标，进行跳转到个人中心界面， overridePendingTransition(0, 0):去掉进场动画
     * @param view
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void onClickMyCenter(View view){
        Intent i = null;
        i=new Intent(getApplicationContext(), MyCenterActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);

    }

}