package com.example.cugerhuo.Fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class FragmentTabAdapter {

    private List<Fragment> fragments; // tab页面对应的Fragment
    private FragmentActivity fragmentActivity; // Fragment所在的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id
    private int currentTab; // 当前Tab页面索引
    private OnTabChangeListener onTabChangeListener;

    public FragmentTabAdapter(FragmentActivity fragmentActivity,
                              List<Fragment> fragments, int fragmentContentId) {
        this.fragments = fragments;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;
        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        try {
            ft.commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null != onTabChangeListener)
            onTabChangeListener.OnTabChanged(0);
    }

    /**
     * 切换tab
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取带动画的FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        // 设置切换动画
//    if (index > currentTab) {
//       ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
//    } else {
//       ft.setCustomAnimations(R.anim.slide_right_in,
//             R.anim.slide_right_out);
//    }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public void setCurrentFragment(int idx) {
        Fragment fragment = fragments.get(idx);
        FragmentTransaction ft = obtainFragmentTransaction(idx);

        getCurrentFragment().onPause(); // 暂停当前tab
        // getCurrentFragment().onStop(); // 暂停当前tab
        if (fragment.isAdded()) {
            // fragment.onStart(); // 启动目标tab的onStart()
            fragment.onResume(); // 启动目标tab的onResume()
        } else {
            ft.add(fragmentContentId, fragment);
        }
        showTab(idx); // 显示目标tab
        ft.commitAllowingStateLoss();
        // 如果设置了切换tab额外功能功能接口
        if (null != onTabChangeListener) {
            onTabChangeListener.OnTabChanged(idx);
        }
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        this.onTabChangeListener = onTabChangeListener;
    }

    public interface OnTabChangeListener {
        public void OnTabChanged(int index);
    }

}
