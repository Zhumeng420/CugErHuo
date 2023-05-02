package com.example.cugerhuo.activity.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 我的发布适配
 * @author carollkarry
 * @time 2023/5/2
 */
public class ViewAdapterPost extends FragmentPagerAdapter {
    /**
     * 创建Fragment数组
     */
    private List<Fragment> mFragments;
    /**
     * 接收从Activity页面传递过来的Fragment数组
     * @param fm
     * @param fragments
     */
    public ViewAdapterPost(@NonNull FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * 得到页面标题
     * @param position The position of the title requested
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "在卖";
            case 1:
                return "已下架";

            default:
                return "";
        }
    }
}
