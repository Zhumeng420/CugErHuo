package com.example.cugerhuo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.activity.adapter.RecyclerViewOnSellAdapter;

import java.util.List;

/**
 * 评价fragment
 * @Author: 李柏睿
 * @Time: 2023/5/10 19:18
 */
public class AppraiseFragment extends Fragment {
    /**
     * title 标题
     **/
    private String title;
    private RecyclerView recyclerViewOnSell;
    private RecyclerViewOnSellAdapter adapter;
    private List<Commodity> a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public AppraiseFragment(String t){
        super();
        this.title=t;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_fragment_appraise, container, false);

        return view;
    }
}
