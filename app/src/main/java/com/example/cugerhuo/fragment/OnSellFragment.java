package com.example.cugerhuo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewOnSellAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * 在卖
 * @author carollkarry
 * @time 2023/5/2
 */
public class OnSellFragment extends Fragment {


    private RecyclerView recyclerViewOnSell;
    private RecyclerViewOnSellAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_sell, container, false);
        recyclerViewOnSell=view.findViewById(R.id.recyclerViewOnSell);
        recyclerViewOnSell.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new RecyclerViewOnSellAdapter(getActivity());
        recyclerViewOnSell.setAdapter(adapter);
       return view;
    }
}
