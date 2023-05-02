package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.UserInfo;

import java.io.File;


/**
 *我的发布商品适配器
 * @author carollkarry
 * @time 2023/5/2
 */
public class RecyclerViewOnSellAdapter extends RecyclerView.Adapter<RecyclerViewOnSellAdapter.ViewHolder>{

    /**
     * context 获取映射文件
     * count item数
     */
    private Context context;
    private int count=20;


    public RecyclerViewOnSellAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_on_sell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /**
         * 设置图片圆角30度
         */
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
        Glide.with(context).load(Uri.fromFile(new File(UserInfo.getUrl())))
                .apply(options)
                .into(holder.goodPostImg);
    }

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

        ImageView goodPostImg;

        public ViewHolder(View itemView) {
            super(itemView);
            goodPostImg=itemView.findViewById(R.id.good_item_post_img);

        }
    }

}
