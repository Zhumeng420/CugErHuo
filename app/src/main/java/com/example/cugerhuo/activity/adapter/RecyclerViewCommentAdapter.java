package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.AddressInfo;
import com.example.cugerhuo.access.user.CommentInfo;

import java.util.List;

/**
 * 评论里面的RecyclerView适配器
 * @Author: 李柏睿
 * @Time: 2023/4/27
 */
public class RecyclerViewCommentAdapter extends RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder> {
    private Context context;
    private int count;
    private static int switchFragment;

    public RecyclerViewCommentAdapter(Context context, List<CommentInfo> commentInfos,int switchFlag) {
        this.context = context;
        count=commentInfos.size();
        switchFragment = switchFlag;
    }


    /**
     * 获取目前RecyclerView中的item数量
     * @Author: 李柏睿
     * @Time: 2023/4/27
     */
    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public RecyclerViewCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new RecyclerViewCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCommentAdapter.ViewHolder holder, int position) {


    }

    /**
     * 寻找到对应控件的id
     * @Author: 李柏睿
     * @Time: 2023/4/22 16:16
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**用户头像*/
        ImageView userImg;
        /**用户名*/
        TextView userName;
        /**评论内容*/
        TextView comment;
        /**出价信息是否可见*/
        LinearLayout bidSwitch;
        /**出价信息*/
        TextView bidNumber;
        private RecyclerViewAdapter.OnItemClickListener mListener;// 声明自定义的接口
        public ViewHolder(View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_comment_img);
            userName = itemView.findViewById(R.id.user_comment_name);
            comment = itemView.findViewById(R.id.comment_context);
            bidSwitch = itemView.findViewById(R.id.bid_switch);
            bidNumber = itemView.findViewById(R.id.bid_number);
            if(switchFragment==0){
                bidSwitch.setVisibility(View.INVISIBLE);
            }else{
                bidSwitch.setVisibility(View.VISIBLE);
                comment.setText("我出了我理想的价格");
            }
        }
    }

}