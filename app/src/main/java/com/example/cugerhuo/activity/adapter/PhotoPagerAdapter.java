package com.example.cugerhuo.activity.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cugerhuo.views.PhotoFragment;

import java.util.ArrayList;

/**
 * 轮播图适配器
 * @Author: 李柏睿
 * @Time: 2023/5/5 0:34
 */
public class PhotoPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<String> urlList;

    public PhotoPagerAdapter(FragmentManager fm, ArrayList<String> urlList) {
        super(fm);
        this.urlList=urlList;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}
