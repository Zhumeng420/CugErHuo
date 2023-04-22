package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cugerhuo.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 首页商品展示recyclerView适配器
 * @author carollkarry
 * @time 2023/4/22
 */
public class RecyclerViewGoodsDisplayAdapter extends RecyclerView.Adapter<RecyclerViewGoodsDisplayAdapter.ViewHolder> {
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
    public RecyclerViewGoodsDisplayAdapter(Context context){
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_display_good, parent, false);
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
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
        Glide.with(context).load(R.drawable.icon_iphone)
                .apply(options)
                .into(holder.good_item_img);
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
        ImageView good_item_img;
        TextView good_item_title;
        TextView goods_item_price;
        RoundedImageView good_item_user_img;
        TextView good_item_username;

        public ViewHolder(View itemView) {
            super(itemView);
            good_item_img=itemView.findViewById(R.id.good_item_img);
            good_item_title=itemView.findViewById(R.id.good_item_title);
            goods_item_price=itemView.findViewById(R.id.goods_item_price);
            good_item_user_img=itemView.findViewById(R.id.good_item_user_img);
            good_item_username=itemView.findViewById(R.id.good_item_username);

        }
    }

    /**
     * 设置item的间距
     * @author carollkarry
     * @time 2023/4/22
     */
    public static class spaceItem extends RecyclerView.ItemDecoration{
        //设置item的间距
        private int space=5;
        public spaceItem(int space){
            this.space=space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            outRect.bottom=space;
            outRect.top=space;
        }
    }

}
