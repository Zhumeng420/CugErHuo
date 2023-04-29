package com.example.cugerhuo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cugerhuo.Fragment.ConcernFragment;
import com.example.cugerhuo.Fragment.ReginFragment;
import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.adapter.ViewAdapter;
import com.example.cugerhuo.activity.imessage.MessageActivity;
import com.example.cugerhuo.fragment.SuggestFragment;
import com.example.cugerhuo.graph.GraphService;
import com.google.android.material.tabs.TabLayout;

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
public class ErHuoActivity extends AppCompatActivity implements View.OnClickListener{


    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author: 唐小莉
     * @time 2023/3/20 16:36
     */
    private ImageView ivTabThree;
    private LinearLayout llTabOne;
    private LinearLayout llTabTwo;
    private LinearLayout llTabFour;
    private LinearLayout llTabFive;
    private LinearLayout llSearch;
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<Fragment>();
    ViewAdapter adapter;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo);
        initView();
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_home_navigation);
        initFragment();
        /**
         * 登录云信
         */
        loginChatAccount();

    }

    /**
     *登录云信
     * @author 施立豪
     * @time 2023/4/29
     */
    private void loginChatAccount()
    {
        GraphService.getInstance(getApplicationContext());
    }
    /**
     * 初始化各个控件，找到对应的组件
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void initView(){
        llTabOne =findViewById(R.id.ll_tab_one);
        llTabOne.setOnClickListener(this);
        llTabTwo =findViewById(R.id.ll_tab_two);
        llTabTwo.setOnClickListener(this);
        llTabFour =findViewById(R.id.ll_tab_four);
        llTabFour.setOnClickListener(this);
        llTabFive =findViewById(R.id.ll_tab_five);
        llTabFive.setOnClickListener(this);
        ivTabThree = (ImageView) findViewById(R.id.iv_tab_three);
        ivTabThree.setOnClickListener(this);
        llSearch = findViewById(R.id.linear_search_item);
        llSearch.setOnClickListener(this);

    }

    /**
     * 底部导航栏点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_tab_one:
                break;
            /**
             * 点击悬赏图标，进行跳转到悬赏界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_two:
                startActivity(new Intent(getApplicationContext(), XuanShangActivity.class));
                overridePendingTransition(0,0);
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
                ivTabThree.startAnimation( animation );
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
                break;
            /**
             * 点击个人中心图标，进行跳转到个人中心界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_five:
                startActivity(new Intent(getApplicationContext(), MyCenterActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击搜索跳转
             * @Author: 李柏睿
             * @Time: 2023/4/12 16:12
             */
            case R.id.linear_search_item:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(0,0);
                break;
            default:
                break;
        }
    }
    /**
     * fragment初始化
     * @Author: 李柏睿
     * @Time: 2023/4/7 20:38
     */
    public void initFragment(){
        /**初始化数据*/
        fragments.add(new ConcernFragment("关注"));
        fragments.add(new SuggestFragment("推荐"));
        fragments.add(new ReginFragment("地区"));

        /**设置ViewPager适配器*/
        adapter = new ViewAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        /**设置初始fragment*/
        viewPager.setCurrentItem(1,false);


        /**
         * @param
         * @return: void
         * @Author: 李柏睿
         * @Time: 2023/4/7 22:24
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(ErHuoActivity.this, R.style.fragment_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(ErHuoActivity.this, R.style.fragment);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /**关联ViewPager*/
        tabLayout.setupWithViewPager(viewPager);
        /**设置固定的tab*/
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


}