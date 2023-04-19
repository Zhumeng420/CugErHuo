package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.XuanShangInfo;
import com.example.cugerhuo.views.MyCustomImageLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 悬赏界面的RecyclerView
 * @Author: 李柏睿
 * @Time: 2023/4/13 15:26
 */

public class RecyclerViewXuanShangAdapter extends RecyclerView.Adapter<RecyclerViewXuanShangAdapter.ViewHolder>{
    private Context context;
    private int count;


    public RecyclerViewXuanShangAdapter(Context context, List<XuanShangInfo> partUserInfo) {
        this.context = context;
        count=partUserInfo.size();
        System.out.println("hhhhhhh"+count);
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_xuanshang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //动态相册集合
        ArrayList<String> imageList = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            imageList.add(UserInfo.getUrl());
        }
        //设置数据
        holder.mPhotoGridView.setUrlListData(imageList);
        System.out.println("nihaone");
    }


    /**
     * 寻找到对应控件的id
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:24
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MyCustomImageLayout mPhotoGridView;
        public ViewHolder(View itemView) {
            super(itemView);
            mPhotoGridView = itemView.findViewById(R.id.image_layout);
            //setImage(6);
        }

        /**
         * 根据要求生成集合个数
         * @param number
         * @Author: 李柏睿
         * @Time: 2023/4/19 20:10
         */
        private void setImage(int number){
            //动态相册集合
            ArrayList<String> imageList = new ArrayList<>();
            for (int i = 0; i < number; i++){
                imageList.add("https://img2.baidu.com/it/u=4067224682,690721702&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=450");
            }
            //设置数据
            mPhotoGridView.setUrlListData(imageList);
        }
    }


}
