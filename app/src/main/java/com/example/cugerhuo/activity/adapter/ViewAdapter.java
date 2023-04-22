package com.example.cugerhuo.activity.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 贰货页viewAdapter重写
 * @author carollkarry
 * @time 2023/4/22
 */
public class ViewAdapter extends FragmentPagerAdapter {


    /**
     * 创建Fragment数组
     */
    private List<Fragment> mFragments;

    /**
     * 接收从Activity页面传递过来的Fragment数组
     * @param fm
     * @param fragments
     */
    public ViewAdapter(FragmentManager fm,List<Fragment> fragments){
        super(fm);
        mFragments = fragments;
    }
    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
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
                return "关注";
            case 1:
                return "推荐";
            case 2:
                return "地区";
            default:
                return "";
        }
    }

}
