package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.AddressInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.XuanShangInfo;
import com.example.cugerhuo.oss.InitOS;
import com.example.cugerhuo.views.MyCustomImageLayout;
import com.luck.picture.lib.interfaces.OnItemClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 地址管理里面的RecyclerView适配器
 * @Author: 李柏睿
 * @Time: 2023/4/22 16:14
 */
public class RecyclerViewAddressAdapter extends RecyclerView.Adapter<RecyclerViewAddressAdapter.ViewHolder> {
    private Context context;
    private int count;
    private OnItemClickListener mClickListener;


    public RecyclerViewAddressAdapter(Context context, List<AddressInfo> partUserInfo) {
        this.context = context;
        count=partUserInfo.size();
    }


    /**
     * 获取目前RecyclerView中的item数量
     * @Author: 李柏睿
     * @Time: 2023/4/22 16:15
     */
    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public RecyclerViewAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new RecyclerViewAddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAddressAdapter.ViewHolder holder, int position) {

        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至编辑界面@Author: 李柏睿
             * @Time: 2023/4/22 19:46
             */
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,position);
            }
        });
    }

    /**
     * 寻找到对应控件的id
     * @Author: 李柏睿
     * @Time: 2023/4/22 16:16
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView editIcon;
        private RecyclerViewAdapter.OnItemClickListener mListener;// 声明自定义的接口
        public ViewHolder(View itemView) {
            super(itemView);
            editIcon = itemView.findViewById(R.id.edit_address);
        }
    }

    /**
     * item点击响应函数
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/4/22 19:15
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @Author: 李柏睿
     * @Time: 2023/4/22 19:28
     */
    public interface OnItemClickListener {
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        public void onItemClick(View view, int position);
    }

}