package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.AddressInfo;
import com.example.cugerhuo.access.user.MessageInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.oss.InitOS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
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
     * partUserInfos 最近联系人信息列表
     * messageInfos 最近聊天信息列表
     */
    private Context context;
    private int count;
    private List<PartUserInfo> partUserInfos;
    private List<MessageInfo> messageInfos;
    private OnItemClickListener mClickListener;

    /**
     * 构造函数
     * @param context 获取映射文件
     * @author 唐小莉
     * @time 2023/4/22
     */
    public RecyclerViewChatAdapter(Context context, List<MessageInfo> partUserInfo, List<PartUserInfo>partUserInfos) {
        this.context = context;
        this.partUserInfos=partUserInfos;
        this.messageInfos=partUserInfo;
        count=partUserInfos.size();
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
        /**
         * 设置聊天对应的用户名
         * @author 唐小莉
         * @time 2023/4/30
         */
        holder.userChatName.setText(partUserInfos.get(position).getUserName());
        /**
         * 获取oss路径
         */
        String url = partUserInfos.get(position).getImageUrl();
        /**
         *  判断头像是否为空，如果为空则使用默认的头像进行显示
         */
        if(url!=null&&!"".equals(url))
        { /**@time 2023/4/30
         * @author 唐小莉
         * 异步更新头像,并实时更新
         */
            OSSClient oss = InitOS.getOssClient();

            /**
             * 获取本地保存路径
             */
            String newUrl= getSandBoxPath(context) + url;
            partUserInfos.get(position).setImageUrl(newUrl);
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
                                holder.userChatImg.setImageURI(Uri.fromFile(new File(newUrl)));
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
                holder.userChatImg.setImageURI(Uri.fromFile(new File(newUrl)));}}

        Timestamp timestamp=new Timestamp(messageInfos.get(position).getChatTime());
        /**
         * 将时间毫秒数与标准日期时间的相互转换
         */
        String time=timestamp.toString().split("\\.")[0];
        holder.userChatTime.setText(time);
        holder.userChatInfo.setText(messageInfos.get(position).getContent());
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
