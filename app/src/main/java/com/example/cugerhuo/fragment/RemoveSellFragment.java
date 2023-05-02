package com.example.cugerhuo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.adapter.RecyclerViewOnSellAdapter;

/**
 * 已下架
 * @author carollkarry
 * @time 2023/5/2
 */
public class RemoveSellFragment extends Fragment {

    private RecyclerView recyclerViewRemoveSell;
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
        View view = inflater.inflate(R.layout.fragment_remove_sell, container, false);
        recyclerViewRemoveSell=view.findViewById(R.id.recyclerViewRemoveSell);
        recyclerViewRemoveSell.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new RecyclerViewOnSellAdapter(getActivity());
        recyclerViewRemoveSell.setAdapter(adapter);
        return view;
    }
}
