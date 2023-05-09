package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.makeramen.roundedimageview.RoundedImageView;


/**
 * 我的评价adapter
 * @author carollkarry
 * @time 2023/5/9
 */
public class RecyclerViewEvaluateAdapter extends RecyclerView.Adapter<RecyclerViewEvaluateAdapter.ViewHolder>{

    private Context context;
    private int count=10;
    private OnItemClickListener mClickListener;

    public RecyclerViewEvaluateAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.evaluate.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至个人主页
             * @param v
             * @author 唐小莉
             * @Time 2023/5/9
             */
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    /**
     * 寻找到对应控件的id
     * @Author: 唐小莉
     * @Time: 2023/5/9
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView buyUserImg;
        TextView buyUsername;
        ImageView goodsBuyImg;
        TextView goodsBuyName;
        TextView goodsBuyPrice;
        Button contactBuy;
        Button evaluate;
        public ViewHolder(View itemView) {
            super(itemView);
            buyUserImg=itemView.findViewById(R.id.user_buy_img);
            buyUsername=itemView.findViewById(R.id.user_buy_name);
            goodsBuyImg=itemView.findViewById(R.id.good_item_buy_img);
            goodsBuyName=itemView.findViewById(R.id.good_item_buy_title);
            goodsBuyPrice=itemView.findViewById(R.id.goods_item_price);
            contactBuy=itemView.findViewById(R.id.contact_buy);
            evaluate=itemView.findViewById(R.id.comment_goods);
        }
    }

    /**
     * item点击响应函数
     * @param listener 监听click
     * @author 唐小莉
     * @time 2023/5/9
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
       this.mClickListener=listener;
    }
    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @author 唐小莉
     * @time 2023/5/9
     */
    public interface OnItemClickListener {
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        public void onItemClick(View view, int position);
    }
}
