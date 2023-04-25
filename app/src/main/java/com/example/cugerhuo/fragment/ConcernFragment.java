package com.example.cugerhuo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cugerhuo.R;

/**
 * 首页关注页
 * @author carollkarry
 * @time 2023/4/22
 */
public class ConcernFragment extends Fragment {
    /**
     * title 标题
     **/
    private String title;

    public ConcernFragment(String title){
        super();
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concern, container, false);
        return view;
    }
    /**
     * 获取标题
     * @return 标题
     * @author 唐小莉
     * @time 20023/4/22
     */
    public String getTitle() {
        return title;
    }
}
