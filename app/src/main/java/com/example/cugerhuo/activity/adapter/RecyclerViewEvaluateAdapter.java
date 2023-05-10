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
import android.widget.ImageView;
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
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.evaluate.EvaluationInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.oss.InitOS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的评价adapter
 * @author carollkarry
 * @time 2023/5/9
 */
public class RecyclerViewEvaluateAdapter extends RecyclerView.Adapter<RecyclerViewEvaluateAdapter.ViewHolder>{

    private Context context;
    private int count=10;
    private OnItemClickListener mClickListener;
    private List<PartUserInfo> evaluationUser;
    private List<Commodity> commodities;

    public RecyclerViewEvaluateAdapter(Context context,List<PartUserInfo> evaluationUser,List<Commodity>commodities){
        this.context=context;
        this.evaluationUser=evaluationUser;
        this.commodities=commodities;
        this.count=evaluationUser.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.evaluate.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至个人主页
             * @param v
             * @author 唐小莉
             * @Time 2023/5/9
             */
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,position);
            }
        });
        setUserImg(holder,position);
        holder.buyUsername.setText(evaluationUser.get(position).getUserName());
        setCommodityImg(holder,position);
        holder.goodsBuyName.setText(commodities.get(position).getDescription());
        holder.goodsBuyPrice.setText(String.valueOf(commodities.get(position).getPrice()));


    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void setCommodityImg(final ViewHolder holder,final int position){
        /**
         * 商品头像
         */
        String url1=commodities.get(position).getUrl1();

        if(url1!=null&&!"".equals(url1))
        {
            String []urls=url1.split(";");
            if(urls.length>0){
                url1=urls[0];}
            int length=urls.length;
            String result[]=new String[length];
            result[length-1]=urls[length-1];
            // 从后往前依次减去后面一个元素
            if(length>1){
                for (int i = length - 2; i >= 0; i--) {
                    String current = urls[i];
                    String next = result[i + 1];
                    int index = current.lastIndexOf(next);
                    if(index>0){
                        result[i ] = current.substring(0, index);}
                    else
                    {
                        result[i]=current;
                    }
                }}
// 将第一个元素赋值给结果数组
            url1=result[0];

        }
        /**
         * 异步加载头像
         */
        /**
         *  判断头像是否为空，如果为空则使用默认的头像进行显示
         */
        if (url1 != null && !"".equals(url1)) { /**@time 2023/4/26
         * @author 唐小莉
         * 异步更新头像,并实时更新
         */
            System.out.println("url11"+url1);
            OSSClient oss = InitOS.getOssClient();

            /**
             * 获取本地保存路径
             */
            String newUrl1 = getSandBoxPath(context) + url1;
            System.out.println("imager2"+url1);
            File f = new File(newUrl1);
            if (!f.exists()) {
                /**
                 * 构建oss请求
                 */
                GetObjectRequest get = new GetObjectRequest("cugerhuo", url1);
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
                                /**
                                 * 设置商品图片圆角30度
                                 */
                                System.out.println("image1"+newUrl1);

                                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
                                Glide.with(context).load(Uri.fromFile(new File(newUrl1)))
                                        .apply(options)
                                        .into(holder.goodsBuyImg);
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
                /**
                 * 设置商品图片圆角30度
                 */
                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
                Glide.with(context).load(Uri.fromFile(new File(newUrl1)))
                        .apply(options)
                        .into(holder.goodsBuyImg);
            }
        }
    }

    /**
     * 设置用户头像
     * @param holder
     * @param position  item位置
     * @author 唐小莉
     * @time 2023/5/10
     */
    public void setUserImg(final ViewHolder holder, final int position){
        /**
         * 获取oss路径
         */
        String url = evaluationUser.get(position).getImageUrl();
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
            evaluationUser.get(position).setImageUrl(newUrl);
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
                                holder.buyUserImg.setImageURI(Uri.fromFile(new File(newUrl)));
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
                holder.buyUserImg.setImageURI(Uri.fromFile(new File(newUrl)));}}
    }

    /**
     * 寻找到对应控件的id
     * @Author: 唐小莉
     * @Time: 2023/5/9
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView buyUserImg;
        TextView buyUsername;
        ImageView goodsBuyImg;
        TextView goodsBuyName;
        TextView goodsBuyPrice;
        Button contactBuy;
        Button evaluate;
        public ViewHolder(View itemView) {
            super(itemView);
            buyUserImg=itemView.findViewById(R.id.user_buy_img);
            buyUsername=itemView.findViewById(R.id.user_buy_name);
            goodsBuyImg=itemView.findViewById(R.id.good_item_buy_img);
            goodsBuyName=itemView.findViewById(R.id.good_item_buy_title);
            goodsBuyPrice=itemView.findViewById(R.id.goods_item_buy_price);
            contactBuy=itemView.findViewById(R.id.contact_buy);
            evaluate=itemView.findViewById(R.id.comment_goods);
        }
    }

    /**
     * item点击响应函数
     * @param listener 监听click
     * @author 唐小莉2
     * @time 2023/5/9
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
       this.mClickListener=listener;
    }
    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @author 唐小莉
     * @time 2023/5/9
     */
    public interface OnItemClickListener {
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        public void onItemClick(View view, int position);
    }
}
