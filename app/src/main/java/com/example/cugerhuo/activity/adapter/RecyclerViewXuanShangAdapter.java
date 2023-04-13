package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.XuanShangInfo;

import java.io.File;
import java.util.List;

/**
 * 悬赏界面的RecyclerView
 * @Author: 李柏睿
 * @Time: 2023/4/13 15:26
 */

public class RecyclerViewXuanShangAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private int count;

    public RecyclerViewXuanShangAdapter(Context context, List<XuanShangInfo> partUserInfo) {
        this.context = context;
        count=partUserInfo.size();
    }

    /**
     * 获取目前RecyclerView中的item数量
     * @Author: 李柏睿
     * @Time: 2023/4/13 15:29
     */
    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_xuanshang, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {

    }
}
