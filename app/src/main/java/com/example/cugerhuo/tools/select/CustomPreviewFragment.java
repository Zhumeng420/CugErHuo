package com.example.cugerhuo.tools.select;

import android.os.Bundle;

import com.luck.picture.lib.PictureSelectorPreviewFragment;
import com.luck.picture.lib.adapter.PicturePreviewAdapter;
/**
 *
 * 选择照片适配器
 * @author 施立豪
 * @date：2023/4/21
 */
public class CustomPreviewFragment extends PictureSelectorPreviewFragment {

    public static CustomPreviewFragment newInstance() {
        CustomPreviewFragment fragment = new CustomPreviewFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return CustomPreviewFragment.class.getSimpleName();
    }

    @Override
    protected PicturePreviewAdapter createAdapter() {
        return new CustomPreviewAdapter();
    }
}
