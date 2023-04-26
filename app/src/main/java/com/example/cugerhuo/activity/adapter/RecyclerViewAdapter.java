package com.example.cugerhuo.activity.adapter;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.nirvana.tools.core.SupportJarUtils.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.OtherPeopleActivity;
import com.example.cugerhuo.oss.InitOS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
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
    private OnItemClickListener mClickListener;
    private OnItemClickListener mClickUserListener;

    public RecyclerViewAdapter(Context context, List<PartUserInfo>PartUserInfo) {
        this.context = context;
        partUserInfo=PartUserInfo;
        count=partUserInfo.size();
    }

    public void setPartUserInfoPosition(int p,int concern){
        partUserInfo.get(p).setConcern(concern);

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

    /**
     * Item响应事件
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //进行item对应控件部分的内容设置
        holder.user_concern_name.setText(partUserInfo.get(position).getUserName());
        holder.user_concern_sign.setText(partUserInfo.get(position).getSignature());
        if(partUserInfo.get(position).getConcern()==0){
            holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
            holder.btn_concerned.setText("关注");
            holder.btn_concerned.setTextColor(Color.RED);
            partUserInfo.get(position).setConcern(0);
        }
        else if(partUserInfo.get(position).getConcern()==2){
            holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_concern);
            holder.btn_concerned.setText("互相关注");
            holder.btn_concerned.setTextColor(Color.parseColor("#9C9898"));
            partUserInfo.get(position).setConcern(2);
        }
        else{
            holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_concern);
            holder.btn_concerned.setText("已关注");
            holder.btn_concerned.setTextColor(Color.parseColor("#9C9898"));
            partUserInfo.get(position).setConcern(1);
        }
        /**
         * 获取oss路径
         */
        String url = partUserInfo.get(position).getImageUrl();
        /**
         *  判断头像是否为空，如果为空则使用默认的头像进行显示
         */
        if(url!=null&&!"".equals(url))
        { /**@time 2023/4/13
         * @author 施立豪
         * 异步更新头像,并实时更新
         */
        OSSClient oss = InitOS.getOssClient();

        /**
         * 获取本地保存路径
         */
        String newUrl= getSandBoxPath(context) + url;
        partUserInfo.get(position).setImageUrl(newUrl);
        System.out.println("newUrlhhhh"+newUrl);
        File f = new File(newUrl);
        if (!f.exists()) {
            /**
             * 构建oss请求
             */
            GetObjectRequest get = new GetObjectRequest("cugerhuo", url);
            /**
             * 异步任务
             */
            oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                /**
                 * 下载成功
                 * @param request
                 * @param result
                 */
                @Override
                public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                    // 开始读取数据。
                    long length = result.getContentLength();
                    if (length > 0) {
                        byte[] buffer = new byte[(int) length];
                        int readCount = 0;
                        while (readCount < length) {
                            try {
                                readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
                            } catch (Exception e) {
                                OSSLog.logInfo(e.toString());
                            }
                        }
                        // 将下载后的文件存放在指定的本地路径，例如D:\\localpath\\exampleobject.jpg。
                        try {
                            FileOutputStream fout = new FileOutputStream(newUrl);
                            fout.write(buffer);
                            fout.close();
                            /**
                             * 下载完成，填写更新逻辑
                             */
                            holder.user_concern_img.setImageURI(Uri.fromFile(new File(newUrl)));
                        } catch (Exception e) {
                            OSSLog.logInfo(e.toString());
                        }
                    }
                }
                @Override
                public void onFailure(GetObjectRequest request, ClientException clientException,
                                      ServiceException serviceException) {
                    Log.e(TAG, "oss下载文件失败");
                }
            });
        } else {
            holder.user_concern_img.setImageURI(Uri.fromFile(new File(newUrl)));}}
        holder.user_concern.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至个人主页
             * @param v
             * @author 唐小莉
             * @Time 2023/4/13
             */
            @Override
            public void onClick(View v) {
                mClickUserListener.onItemClick(v,position);
            }
        });

        holder.btn_concerned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,position);
            }

        });
    }

    /**
     * 用于响应获取取消关注成功后进行样式的改变
     * @param holder
     * @param position
     * @param payloads
     * @author 唐小莉
     * @time 2023/4/13
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position,@NonNull List<Object> payloads){
        System.out.println(">>>>>payloads"+payloads);
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder,position,payloads);
            return;
        }
        for(Object payload:payloads){
            switch (String.valueOf(payload)){
                /**
                 * 初始为关注状态进行点击取消关注操作（此时非互相关注状态）
                 */
                case "1":
                    holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
                    holder.btn_concerned.setText("关注");
                    holder.btn_concerned.setTextColor(Color.RED);
                    partUserInfo.get(position).setConcern(0);
                    break;
                /**
                 * 点击关注后，两者变为互关状态
                 */
                case "2":
                    holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_concern);
                    holder.btn_concerned.setText("互相关注");
                    holder.btn_concerned.setTextColor(Color.parseColor("#9C9898"));
                    partUserInfo.get(position).setConcern(2);
                    break;
                /**
                 * 两者不是互相关注状态，单方面关注点击
                 */
                case "0":
                    holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_concern);
                    holder.btn_concerned.setText("已关注");
                    holder.btn_concerned.setTextColor(Color.parseColor("#9C9898"));
                    partUserInfo.get(position).setConcern(1);
                    break;
                /**
                 * 初始为互相关注状态，点击取消关注
                 */
                case "3":
                    holder.btn_concerned.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
                    holder.btn_concerned.setText("关注");
                    holder.btn_concerned.setTextColor(Color.RED);
                    partUserInfo.get(position).setConcern(3);
                    break;
                default:
                    break;
            }
        }
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
        private OnItemClickListener mListener;// 声明自定义的接口

        public ViewHolder(View itemView) {
            super(itemView);
            user_concern_name = itemView.findViewById(R.id.user_concern_name);
            user_concern_img=itemView.findViewById(R.id.user_concern_img);
            user_concern_sign=itemView.findViewById(R.id.user_concern_sign);
            btn_concerned=itemView.findViewById(R.id.btn_concerned);
            user_concern=itemView.findViewById(R.id.user_concern);
        }
    }

    /**
     * item点击响应函数
     * @param listener 监听click
     * @author 唐小莉
     * @time 2023/4/13
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnItemUserClickListener(OnItemClickListener listener) {
        this.mClickUserListener = listener;
    }
    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @author 唐小莉
     * @time 2023/4/13
     */
    public interface OnItemClickListener {
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        public void onItemClick(View view, int position);
    }


}