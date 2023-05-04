package com.example.cugerhuo.views;
 
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 轮播图
 * @Author: 李柏睿
 * @Time: 2023/5/5 0:33
 */
public class ViewPagerFixed extends ViewPager {
 
    public ViewPagerFixed(Context context) {
        super(context);
    }
 
    public ViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
