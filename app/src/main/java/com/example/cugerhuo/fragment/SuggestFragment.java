package com.example.cugerhuo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;

/**
 * 首页推荐页
 * @author carollkarry
 * @time 2023/4/22
 */
public class SuggestFragment extends Fragment {
    /**
     * title 标题
     * adapter 适配器
     * goodsRecyclerView 商品recycler
     */
    private String title;
    private RecyclerViewGoodsDisplayAdapter adapter;
    private RecyclerView goodsRecyclerView;

    /**
     * 构造函数
     * @param title
     */
    public SuggestFragment(String title){
        super();
        this.title=title;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);
        /**初始化adapter**/
        adapter=new RecyclerViewGoodsDisplayAdapter(this.getContext());
        goodsRecyclerView =view.findViewById(R.id.display_good_block);
        /**禁止recyclerView滑动**/
        goodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclerView网格布局，即横向排两个item**/
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        /**设置每个item之间的间距**/
        goodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));
        goodsRecyclerView.setAdapter(adapter);
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
