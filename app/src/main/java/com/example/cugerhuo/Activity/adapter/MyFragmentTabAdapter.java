package com.example.cugerhuo.Activity.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mtitle;

    public MyFragmentTabAdapter(@NonNull FragmentManager fm, List<Fragment> mFragments,List<String> mtitle) {
        super(fm);
        this.mFragments=mFragments;
        this.mtitle=mtitle;
    }

    @NonNull
    @Override//返回指定fragment
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override //返回fragment数量
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override  //获取分页标题
    public CharSequence getPageTitle(int position) {
        return mtitle.get(position);
    }
}
