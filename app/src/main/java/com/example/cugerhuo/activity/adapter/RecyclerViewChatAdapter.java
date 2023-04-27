package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.AddressInfo;
import com.example.cugerhuo.access.user.MessageInfo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * 聊天界面recycler适配器重写
 * @author carollkarry
 * @time 2023/4/22
 */
public class RecyclerViewChatAdapter extends RecyclerView.Adapter<RecyclerViewChatAdapter.ViewHolder>{

    /**
     * context 获取映射文件
     * count item数量
     */
    private Context context;
    private int count;
    private OnItemClickListener mClickListener;

    /**
     * 构造函数
     * @param context 获取映射文件
     * @author 唐小莉
     * @time 2023/4/22
     */
    public RecyclerViewChatAdapter(Context context, List<MessageInfo> partUserInfo) {
        this.context = context;
        count=partUserInfo.size();
    }

    /**
     * 对应每个子view item
     * @param parent
     * @param viewType
     * @return
     * @author 唐小莉
     * @Time 2023/4/22
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Item响应事件，获取数据并显示到对应控件
     * @param holder ViewHolder
     * @param position 点击的对应item的位置
     * @author 唐小莉
     * @time 2023/4/22
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewChatAdapter.ViewHolder holder, int position) {
        /**
         * 设置商品图片圆角30度
         */
//        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
//        Glide.with(context).load(R.drawable.icon_iphone)
//                .apply(options)
//                .into(holder.goodItemImg);

        holder.userChat.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至编辑界面@Author: 李柏睿
             * @Time: 2023/4/26
             */
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,position);
            }
        });

    }

    /**
     * 获取该recycler的item数
     * @return
     */
    @Override
    public int getItemCount() {
        return count;
    }


    /**
     * 初始化控件
     * @author carollkarry
     * @time 2023/4/22
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
       LinearLayout userChat;
       RoundedImageView userChatImg;
       TextView userChatName;
       TextView userChatInfo;
       TextView userChatTime;
        private RecyclerViewAdapter.OnItemClickListener mListener;// 声明自定义的接口
        public ViewHolder(View itemView) {
            super(itemView);
            userChat =itemView.findViewById(R.id.userChat);
            userChatImg =itemView.findViewById(R.id.userChatImg);
            userChatName =itemView.findViewById(R.id.userChatName);
            userChatInfo =itemView.findViewById(R.id.userChatInfo);
            userChatTime =itemView.findViewById(R.id.userChatTime);
        }
    }

    /**
     * item点击响应函数
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/4/26
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @Author: 李柏睿
     * @Time: 2023/4/26
     */
    public interface OnItemClickListener {
        /**参数（父组件，当前单击的View,单击的View的位置，数据）*/
        public void onItemClick(View view, int position);
    }
}
