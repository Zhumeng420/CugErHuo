//package com.example.cugerhuo.Activity.adapter;
//
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.example.cugerhuo.Activity.ErHuoActivity;
//
//
//import java.util.List;
//
//public class MyFragmentTabAdapter extends FragmentPagerAdapter {
//    private final int PAGER_COUNT = 5;
//
//    private ErHuoFragment erHuoFragment;
//    private XuanShangFragment xuanShangFragment;
//    private PostFragment postFragment;
//    private MessageFragment messageFragment;
//    private MyCenterFragment myCenterFragment;
//
//
//
//    public MyFragmentTabAdapter(FragmentManager fm) {
//        super(fm);
//        erHuoFragment =new ErHuoFragment();
//        xuanShangFragment =new XuanShangFragment();
//        postFragment =new PostFragment();
//        messageFragment=new MessageFragment();
//        myCenterFragment=new MyCenterFragment();
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = erHuoFragment;
//                break;
//            case 1:
//                fragment = xuanShangFragment;
//                break;
//            case 2:
//                fragment = postFragment;
//                break;
//            case 3:
//                fragment = messageFragment;
//                break;
//            case 4:
//                fragment = myCenterFragment;
//                break;
//        }
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        return PAGER_COUNT;
//    }
//
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
//    }
//
//}
