package com.example.cugerhuo.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.OtherPeopleActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

/**
 * @Author 唐小莉
 * @Description 为自定义RecyclerView适配器,适配关注列表信息
 * @Date 2023/4/4 22：13
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    /**
     * partUserInfo 关注的用户信息
     * count 关注列表的Item个数
     * @author 唐小莉
     * @Time 2023/4/7 22：16
     */
    private Context context;
    private List<PartUserInfo> partUserInfo;
    private int count;

    public RecyclerViewAdapter(Context context, List<PartUserInfo>partUserInfo) {
        this.context = context;
        this.partUserInfo=partUserInfo;
        count=partUserInfo.size();
    }

    /**
     * 获取目前RecyclerView中的item数量
     * @return 数量
     * @author 唐小莉
     * @time 2023/4/7 22:26
     */
    @Override
    public int getItemCount() {
        return count;
    }

    public int getCount() {
        return count;
    }

    /**
     * 对应每个子view item
     * @param parent
     * @param viewType
     * @return
     * @author 唐小莉
     * @Time 2023/4/7 17：23
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_concern, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //进行item对应控件部分的内容设置
        holder.user_concern_name.setText(partUserInfo.get(position).getUserName());
        holder.user_concern_sign.setText(partUserInfo.get(position).getSignature());

        if ("".equals(partUserInfo.get(position).getImageUrl())) {
            holder.user_concern_img.setImageURI(Uri.fromFile(new File(partUserInfo.get(position).getImageUrl())));
        }


        holder.user_concern.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件
             * @param v
             * @author
             * @Time
             */
            @Override
            public void onClick(View v) {
                Toast.makeText(context,holder.user_concern_name.getText().toString(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, OtherPeopleActivity.class);
                intent.putExtra("id",partUserInfo.get(position).getId());
                System.out.println("hhhhhhhhhhhhhhh"+partUserInfo.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    /**
     * 寻找到对应控件的id
     * user_concern_name 用户昵称
     * user_concern_img 用户头像
     * user_concern_sign 用户介绍
     * btn_concerned 已关注按钮
     * @author 唐小莉
     * @Time 2023/4/7 17:25
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_concern_name;
        RoundedImageView user_concern_img;
        TextView user_concern_sign;
        Button btn_concerned;
        LinearLayout user_concern;

        public ViewHolder(View itemView) {
            super(itemView);
            user_concern_name = itemView.findViewById(R.id.user_concern_name);
            user_concern_img=itemView.findViewById(R.id.user_concern_img);
            user_concern_sign=itemView.findViewById(R.id.user_concern_sign);
            btn_concerned=itemView.findViewById(R.id.btn_concerned);
            user_concern=itemView.findViewById(R.id.user_concern);
        }
    }

}
