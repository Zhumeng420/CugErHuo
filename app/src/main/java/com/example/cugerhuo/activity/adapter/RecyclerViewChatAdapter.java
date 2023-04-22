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
import com.makeramen.roundedimageview.RoundedImageView;

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
    private int count=20;

    /**
     * 构造函数
     * @param context 获取映射文件
     * @author 唐小莉
     * @time 2023/4/22
     */
    public RecyclerViewChatAdapter(Context context){
        this.context=context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**
         * 设置商品图片圆角30度
         */
//        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
//        Glide.with(context).load(R.drawable.icon_iphone)
//                .apply(options)
//                .into(holder.goodItemImg);
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
        public ViewHolder(View itemView) {
            super(itemView);

            userChat =itemView.findViewById(R.id.userChat);
            userChatImg =itemView.findViewById(R.id.userChatImg);
            userChatName =itemView.findViewById(R.id.userChatName);
            userChatInfo =itemView.findViewById(R.id.userChatInfo);
            userChatTime =itemView.findViewById(R.id.userChatTime);
        }
    }
}
