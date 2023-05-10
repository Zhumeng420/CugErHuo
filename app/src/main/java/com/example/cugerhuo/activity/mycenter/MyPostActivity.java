package com.example.cugerhuo.activity.mycenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
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
    List<Commodity> myPosts=new ArrayList<>();
    private final MyHandler MyHandler = new MyHandler();
    ViewAdapterPost adapter;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        viewPager=findViewById(R.id.viewPager_post);
        tabLayout=findViewById(R.id.tab_post_navigation);initFragment();
        Intent intent = getIntent();
        //从intent取出bundle
        Bundle bundle = intent.getBundleExtra("post");
        if(bundle.getSerializable("postList")!=null)
        {
            myPosts= (List<Commodity>) bundle.getSerializable("postList");
            if(myPosts!=null)
            {
                fragments.add(new OnSellFragment(myPosts));
                fragments.add(new RemoveSellFragment());
                adapter = new ViewAdapterPost(getSupportFragmentManager(),fragments);
                viewPager.setAdapter(adapter);
            }
        }

    }

    /**
     * fragment初始化
     * @Author: 唐小莉
     * @Time: 2023/5/2
     */
    public void initFragment(){
        /**初始化数据*/

        /**设置ViewPager适配器*/

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
    /**
     * 消息发送接收，异步更新UI
     * @author 施立豪
     * @time 2023/3/26
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 更新我上架的商品
                 */
                case 1:

                    break;
                /**
                 * 更新粉丝数
                 * @time 2023/3/27
                 */
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    }

}