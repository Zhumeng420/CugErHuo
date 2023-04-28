package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.Msg;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.oss.InitOS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 聊天中RecyclerView的适配器类
 * @Author: 李柏睿
 * @Time: 2023/4/26 17:52
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{

    private Context context;
    private List<Msg> list;
    private PartUserInfo chatUser;
    public MsgAdapter(List<Msg> list, PartUserInfo chatUser,Context context){
        this.list = list;
        this.chatUser=chatUser;
        this.context=context;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        TextView left_msg;

        LinearLayout rightLayout;
        TextView right_msg;

        RoundedImageView incomingAvatar;
        RoundedImageView outcomingAvatar;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);

            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
            incomingAvatar=view.findViewById(R.id.incoming_avatar);
            outcomingAvatar=view.findViewById(R.id.outcoming_avatar);
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
            if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
            {
                holder.incomingAvatar.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
            }
        }else if(msg.getType() == Msg.TYPE_SEND){
            /**如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏*/
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(msg.getContent());

            /**同样使用View.GONE*/
            holder.leftLayout.setVisibility(View.GONE);
            if (!"".equals(UserInfo.getUrl())&&UserInfo.getUrl()!=null)
            {
                holder.outcomingAvatar.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
            }
        }



//        /**
//         * 获取oss路径
//         */
//        String url = chatUser.getImageUrl();
//        /**
//         *  判断头像是否为空，如果为空则使用默认的头像进行显示
//         */
//        if(url!=null&&!"".equals(url))
//        { /**@time 2023/4/28
//         * @author 唐小莉
//         * 异步更新头像,并实时更新
//         */
//            OSSClient oss = InitOS.getOssClient();
//
//            /**
//             * 获取本地保存路径
//             */
//            String newUrl= getSandBoxPath(context) + url;
//            chatUser.setImageUrl(newUrl);
//            File f = new File(newUrl);
//            if (!f.exists()) {
//                /**
//                 * 构建oss请求
//                 */
//                GetObjectRequest get = new GetObjectRequest("cugerhuo", url);
//                /**
//                 * 异步任务
//                 */
//                oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
//                    /**
//                     * 下载成功
//                     * @param request
//                     * @param result
//                     */
//                    @Override
//                    public void onSuccess(GetObjectRequest request, GetObjectResult result) {
//                        // 开始读取数据。
//                        long length = result.getContentLength();
//                        if (length > 0) {
//                            byte[] buffer = new byte[(int) length];
//                            int readCount = 0;
//                            while (readCount < length) {
//                                try {
//                                    readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
//                                } catch (Exception e) {
//                                    OSSLog.logInfo(e.toString());
//                                }
//                            }
//                            // 将下载后的文件存放在指定的本地路径，例如D:\\localpath\\exampleobject.jpg。
//                            try {
//                                FileOutputStream fout = new FileOutputStream(newUrl);
//                                fout.write(buffer);
//                                fout.close();
//                                /**
//                                 * 下载完成，填写更新逻辑
//                                 */
//                                holder.incomingAvatar.setImageURI(Uri.fromFile(new File(newUrl)));
//                            } catch (Exception e) {
//                                OSSLog.logInfo(e.toString());
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(GetObjectRequest request, ClientException clientException,
//                                          ServiceException serviceException) {
//                        Log.e(TAG, "oss下载文件失败");
//                    }
//                });
//            } else {
//                holder.incomingAvatar.setImageURI(Uri.fromFile(new File(newUrl)));}}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}