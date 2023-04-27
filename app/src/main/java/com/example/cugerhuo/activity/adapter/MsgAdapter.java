package com.example.cugerhuo.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.Msg;

import java.util.List;

/**
 * 聊天中RecyclerView的适配器类
 * @Author: 李柏睿
 * @Time: 2023/4/26 17:52
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{

    private List<Msg> list;
    public MsgAdapter(List<Msg> list){
        this.list = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        TextView left_msg;

        LinearLayout rightLayout;
        TextView right_msg;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);

            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = list.get(position);
        if(msg.getType() == Msg.TYPE_RECEIVED){
            /**如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏*/
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(msg.getContent());
            /**注意此处隐藏右面的消息布局用的是 View.GONE*/
            holder.rightLayout.setVisibility(View.GONE);
        }else if(msg.getType() == Msg.TYPE_SEND){
            /**如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏*/
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(msg.getContent());

            /**同样使用View.GONE*/
            holder.leftLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}