package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.cugerhuo.access.Reward;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.oss.InitOS;
import com.example.cugerhuo.views.MyCustomImageLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 悬赏界面的RecyclerView
 * @Author: 李柏睿
 * @Time: 2023/4/13 15:26
 */

public class RecyclerViewXuanShangAdapter extends RecyclerView.Adapter<RecyclerViewXuanShangAdapter.ViewHolder>{
    private Context context;
    private int count;
    private OnItemClickListener mClickListener;

private List<Reward> rewardList;
private List<PartUserInfo>userInfoList;
    public RecyclerViewXuanShangAdapter(Context context, List<Reward> rewardList, List<PartUserInfo>userInfoList ) {
        this.context = context;
        this.rewardList=rewardList;
        this.userInfoList=userInfoList;
        count=rewardList.size();
    }

    /**
     * 获取目前RecyclerView中的item数量
     * @Author: 李柏睿
     * @Time: 2023/4/13 15:29
     */
    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_xuanshang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //动态相册集合
        ArrayList<String> imageList = new ArrayList<>();

        holder.userName.setText(userInfoList.get(position).getUserName());
        holder.rewardDes.setText(rewardList.get(position).getDescription());
        /**
         * 用户头像设置
         */

        {
            String url = userInfoList.get(position).getImageUrl();
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
                                    holder.userImage.setImageURI(Uri.fromFile(new File(newUrl)));
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
                    holder.userImage.setImageURI(Uri.fromFile(new File(newUrl)));}}
        }
        /**
         * 商品图片设置
         */

        {
            String url1=rewardList.get(position).getUrl();

            if(url1!=null&&!"".equals(url1))
            {
                String []urls=url1.split(";");
                //下载图片列表
                for(String url:urls)
                {
                    if (url != null && !"".equals(url)) { /**@time 2023/4/26
                     * @author 唐小莉
                     * 异步更新头像,并实时更新
                     */
                        System.out.println("url11"+url);
                        OSSClient oss = InitOS.getOssClient();

                        /**
                         * 获取本地保存路径
                         */
                        String newUrl1 = getSandBoxPath(context) + url;
                        System.out.println("imager2"+url);
                        File f = new File(newUrl1);
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
                                 *
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
                                            FileOutputStream fout = new FileOutputStream(newUrl1);
                                            fout.write(buffer);
                                            fout.close();
                                            /**
                                             * 下载完成，填写更新逻辑
                                             */
                                            imageList.add(newUrl1);
                                            /**
                                             * 设置商品图片圆角30度
                                             */

                                        } catch (Exception e) {
                                            OSSLog.logInfo(e.toString());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(GetObjectRequest request, ClientException clientException,
                                                      ServiceException serviceException) {
                                    System.out.println("image3"+newUrl1);
                                    Log.e(TAG, "oss下载文件失败");
                                }
                            });
                        } else {
                            System.out.println("image2"+newUrl1);
                            imageList.add(newUrl1);
                            /**
                             * 设置商品图片圆角30度
                             */
                        }
                    }
                }
                holder.mPhotoGridView.setUrlListData(imageList);
            }else
            {
                holder.mPhotoGridView.setUrlListData(new ArrayList<>());
            }
        }
        //设置数据

        holder.mPhotoGridView.setOnImageClickListener((i, imageGroupList, urlList) -> {
            //图片下标+1
            int index = (int)i.getTag() + 1;
            //单个图片的点击事件
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至发消息
             */
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,position);
            }
        });
    }


    /**
     * 寻找到对应控件的id
     * @Author: 李柏睿
     * @Time: 2023/4/19 20:24
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MyCustomImageLayout mPhotoGridView;
        RoundedImageView userImage;
        TextView userName;
        TextView rewardDes;
        Button chat;
        private RecyclerViewAdapter.OnItemClickListener mListener;// 声明自定义的接口

        public ViewHolder(View itemView) {
            super(itemView);
            mPhotoGridView = itemView.findViewById(R.id.image_layout);
            userImage=itemView.findViewById(R.id.user_concern_img);
            userName=itemView.findViewById(R.id.user_concern_name);
            rewardDes=itemView.findViewById(R.id.rewardDescrip);
            chat=itemView.findViewById(R.id.chat);
            //setImage(6);
        }

        /**
         * 根据要求生成集合个数
         * @param number
         * @Author: 李柏睿
         * @Time: 2023/4/19 20:10
         */
        private void setImage(int number){
            //动态相册集合
            ArrayList<String> imageList = new ArrayList<>();
            for (int i = 0; i < number; i++){
                imageList.add("https://img2.baidu.com/it/u=4067224682,690721702&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=450");
            }
            //设置数据
            mPhotoGridView.setUrlListData(imageList);
        }
    }


    /**
     * item点击响应函数
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/5/10
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @Author: 李柏睿
     * @Time: 2023/5/10
     */
    public interface OnItemClickListener {
        /**参数（父组件，当前单击的View,单击的View的位置，数据）*/
        public void onItemClick(View view, int position);
    }

}
