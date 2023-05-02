package com.example.cugerhuo.activity.mycenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.adapter.ViewAdapterPost;
import com.example.cugerhuo.fragment.OnSellFragment;
import com.example.cugerhuo.fragment.RemoveSellFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 我发布的界面
 * @author carollkarry
 * @time 2023/5/2
 */
public class MyPostActivity extends AppCompatActivity {


    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<Fragment>();
    ViewAdapterPost adapter;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        viewPager=findViewById(R.id.viewPager_post);
        tabLayout=findViewById(R.id.tab_post_navigation);
        initFragment();
    }

    /**
     * fragment初始化
     * @Author: 唐小莉
     * @Time: 2023/5/2
     */
    public void initFragment(){
        /**初始化数据*/
        fragments.add(new OnSellFragment());
        fragments.add(new RemoveSellFragment());
        /**设置ViewPager适配器*/
        adapter = new ViewAdapterPost(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        /**设置初始fragment*/
        viewPager.setCurrentItem(0,false);


        /**
         * @param
         * @return: void
         * @Author: 唐小莉
         * @Time: 2023/5/2
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_post_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(MyPostActivity.this, R.style.fragment_post_selected);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_post_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(MyPostActivity.this, R.style.fragmentPost);
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