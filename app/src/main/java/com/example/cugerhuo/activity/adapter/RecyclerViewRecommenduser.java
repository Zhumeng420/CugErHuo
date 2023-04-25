package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 推荐关注Item
 * @author carollkarry
 * @time 2023/4/25
 */
public class RecyclerViewRecommenduser extends RecyclerView.Adapter<RecyclerViewRecommenduser.ViewHolder>{

    /**
     * context 获取映射文件
     * count item数量
     */
    private Context context;
    private int count=20;

    public RecyclerViewRecommenduser(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommenduser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return count;
    }

    /**
     * 初始化控件
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView userRecImg;
        TextView userRecName;
        TextView userRecFans;
        Button btnRecConcern;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userRecImg=itemView.findViewById(R.id.user_rec_img);
            userRecName=itemView.findViewById(R.id.user_re_name);
            userRecFans=itemView.findViewById(R.id.user_rec_fans);
            btnRecConcern=itemView.findViewById(R.id.btn_rec_concern);
        }
    }

}
