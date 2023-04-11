package com.example.cugerhuo.activity.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cugerhuo.Fragment.MyFragment;

import java.util.ArrayList;

/**
 * ViewPager适配器
 * @Author: 李柏睿
 * @Time: 2023/3/28 9:53
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<MyFragment> fragments;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<MyFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * 得到页面标题
     * @param position The position of the title requested
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
