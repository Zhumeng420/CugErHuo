package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
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
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Pricing;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.oss.InitOS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 评论里面的RecyclerView适配器
 * @Author: 李柏睿
 * @Time: 2023/4/27
 */
public class RecyclerViewPricingAdapter extends RecyclerView.Adapter<RecyclerViewPricingAdapter.ViewHolder> {
    private Context context;
    private int count;
    private static int switchFragment;
    List<Pricing> comments;
    List<PartUserInfo> users;

    public RecyclerViewPricingAdapter(Context context, Map.Entry<List<Pricing>, List<PartUserInfo>> commentInfos, int switchFlag) {
        this.context = context;
        count=commentInfos.getKey().size();
        comments=commentInfos.getKey();
        users=commentInfos.getValue();
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
    public RecyclerViewPricingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new RecyclerViewPricingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewPricingAdapter.ViewHolder holder, int position) {
    holder.userName.setText(users.get(position).getUserName());
    holder.comment.setText(String.valueOf(comments.get(position).getPrice()));
        /**
         * 用户头像设置
         */

        {
            String url = users.get(position).getImageUrl();
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
                                    holder.userImg.setImageURI(Uri.fromFile(new File(newUrl)));
                                    Log.e(TAG, "oss下载文件成功");
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
                    holder.userImg.setImageURI(Uri.fromFile(new File(newUrl)));}}
        }

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