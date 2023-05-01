package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.oss.InitOS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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
    private List<PartUserInfo> partUserInfo;
    private int count;
    private RecyclerViewAdapter.OnItemClickListener mClickListener;
    private RecyclerViewAdapter.OnItemClickListener mClickUserListener;

    public RecyclerViewRecommenduser(Context context,List<PartUserInfo>partUserInfo){
        this.context=context;
        this.partUserInfo=partUserInfo;
        this.count=partUserInfo.size();
    }


    public void setPartUserInfoPosition(int p,int concern){
        partUserInfo.get(p).setConcern(concern);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommenduser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userRecName.setText(partUserInfo.get(position).getUserName());
        holder.userReDesc.setText(partUserInfo.get(position).getSignature());
        if(partUserInfo.get(position).getConcern()==0){
            holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
            holder.btnRecConcern.setText("关注");
            holder.btnRecConcern.setTextColor(Color.RED);
            partUserInfo.get(position).setConcern(0);
        }
        else if(partUserInfo.get(position).getConcern()==2){
            holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_concern);
            holder.btnRecConcern.setText("互相关注");
            holder.btnRecConcern.setTextColor(Color.parseColor("#9C9898"));
            partUserInfo.get(position).setConcern(2);
        }
        else if(partUserInfo.get(position).getConcern()==1){
            holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_concern);
            holder.btnRecConcern.setText("已关注");
            holder.btnRecConcern.setTextColor(Color.parseColor("#9C9898"));
            partUserInfo.get(position).setConcern(1);
        }
        else {
            holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
            holder.btnRecConcern.setText("关注");
            holder.btnRecConcern.setTextColor(Color.RED);
            partUserInfo.get(position).setConcern(3);
        }

        setUserImg(holder,position);

        /**
         * 点击推荐用户item 进行跳转
         * @author 唐小莉
         * @time 2023/4/30
         */
        holder.userRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickUserListener.onItemClick(view,position);
            }
        });
        /**
         * 关注点击事件
         * @author 唐小莉
         * @time 2023/4/30
         */
        holder.btnRecConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,position);
            }
        });
    }

    /**
     * 设置头像
     * @param holder
     * @param position  item位置
     * @author 唐小莉
     * @time 2023/4/30
     */
    public void setUserImg(final ViewHolder holder, final int position){
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
                                holder.userRecImg.setImageURI(Uri.fromFile(new File(newUrl)));
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
                holder.userRecImg.setImageURI(Uri.fromFile(new File(newUrl)));}}
    }
    /**
     * 用于响应获取取消关注成功后进行样式的改变
     * @param holder
     * @param position
     * @param payloads
     * @author 唐小莉
     * @time 2023/4/30
     */
    @Override
    public void onBindViewHolder(final RecyclerViewRecommenduser.ViewHolder holder, final int position, @NonNull List<Object> payloads){
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
                    holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
                    holder.btnRecConcern.setText("关注");

                    holder.btnRecConcern.setTextColor(Color.RED);
                    partUserInfo.get(position).setConcern(0);
                    break;
                /**
                 * 点击关注后，两者变为互关状态
                 */
                case "2":
                    holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_concern);
                    holder.btnRecConcern.setTextColor(Color.parseColor("#9C9898"));
                    partUserInfo.get(position).setConcern(2);
                    holder.btnRecConcern.setText("互相关注");
                    break;
                /**
                 * 两者不是互相关注状态，单方面关注点击
                 */
                case "0":
                    holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_concern);
                    holder.btnRecConcern.setText("已关注");
                    holder.btnRecConcern.setTextColor(Color.parseColor("#9C9898"));
                    partUserInfo.get(position).setConcern(1);
                    break;
                /**
                 * 初始为互相关注状态，点击取消关注
                 */
                case "3":
                    holder.btnRecConcern.setBackgroundResource(R.drawable.shape_btn_cancel_concern);
                    holder.btnRecConcern.setText("关注");
                    holder.btnRecConcern.setTextColor(Color.RED);
                    partUserInfo.get(position).setConcern(3);
                    break;
                default:
                    break;
            }
        }
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
        TextView userReDesc;
        LinearLayout userRe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userRecImg=itemView.findViewById(R.id.user_rec_img);
            userRecName=itemView.findViewById(R.id.user_re_name);
            userRecFans=itemView.findViewById(R.id.user_rec_fans);
            btnRecConcern=itemView.findViewById(R.id.btn_rec_concern);
            userReDesc=itemView.findViewById(R.id.user_re_desc);
            userRe=itemView.findViewById(R.id.user_re);
        }
    }


    /**
     * item点击响应函数
     * @param listener 监听click
     * @author 唐小莉
     * @time 2023/4/30
     */
    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setOnItemUserClickListener(RecyclerViewAdapter.OnItemClickListener listener) {
        this.mClickUserListener = listener;
    }
    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @author 唐小莉
     * @time 2023/4/30
     */
    public interface OnItemClickListener {
        /**
         * 点击
         * @param view
         * @param position
         */
        public void onItemClick(View view, int position);
    }

}
