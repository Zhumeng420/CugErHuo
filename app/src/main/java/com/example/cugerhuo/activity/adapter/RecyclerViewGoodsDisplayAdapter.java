package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.comment.CommentOperate;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.access.pricing.PricingOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.oss.InitOS;
import com.example.cugerhuo.tools.GetFileNameUtil;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.tools.TracingHelper;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import io.lettuce.core.api.sync.RedisCommands;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * 首页商品展示recyclerView适配器
 * @author carollkarry
 * @time 2023/4/22
 */
public class RecyclerViewGoodsDisplayAdapter extends RecyclerView.Adapter<RecyclerViewGoodsDisplayAdapter.ViewHolder> {
    /**
     * context 获取映射文件
     * count item数量
     */
    private Context context;
    private int count=20;
    private OnItemClickListener mClickListener;
    private List<Commodity> commodities;
    private List<PartUserInfo> userInfos;
    /**
     * 构造函数
     * @param context 获取映射文件
     * @author 唐小莉
     * @time 2023/4/22
     */
    public RecyclerViewGoodsDisplayAdapter(Context context,List<Commodity> commodities,List<PartUserInfo> userInfos){
        this.context=context;
        this.commodities=commodities;
        this.userInfos=userInfos;
        this.count=commodities.size();
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_display_good, parent, false);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String firstCate= GetFileNameUtil.getCate(commodities.get(position).getCategory());
        holder.goodItemTitle.setText(commodities.get(position).getDescription());
        commodities.get(position).setCategory(firstCate);
        holder.goodsItemPrice.setText(String.valueOf(commodities.get(position).getPrice()));
        holder.goodItemUsername.setText(userInfos.get(position).getUserName());
        Tracer tracer = GlobalTracer.get();
        // 创建spann
        Span span = tracer.buildSpan("oss加载头像和商品主页图片（一次）").withTag("RecyclerViewGoodsDisplayAdapter：", "onBindViewHolder").start();
        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
            // 业务逻辑写这里
        /**
         * 用户头像
         */
        String url=userInfos.get(position).getImageUrl();
        {
            /**
             * 异步加载头像
             */
            /**
             *  判断头像是否为空，如果为空则使用默认的头像进行显示
             */
            if (url != null && !"".equals(url)) { /**@time 2023/4/26
             * @author 唐小莉
             * 异步更新头像,并实时更新
             */
                OSSClient oss = InitOS.getOssClient();

                /**
                 * 获取本地保存路径
                 */
                String newUrl = getSandBoxPath(context) + url;

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
                                    FileOutputStream fout = new FileOutputStream(newUrl);
                                    fout.write(buffer);
                                    fout.close();
                                    /**
                                     * 下载完成，填写更新逻辑
                                     */
                                    System.out.println("image"+newUrl);
                                    holder.goodItemUserImg.setImageURI(Uri.fromFile(new File(newUrl)));

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
                    holder.goodItemUserImg.setImageURI(Uri.fromFile(new File(newUrl)));
                }
            }
        }
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
                                        .into(holder.goodItemImg);
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
                        .into(holder.goodItemImg);
            }
        }

    } catch (Exception e) {
        TracingHelper.onError(e, span);
        throw e;
    } finally {
        span.finish();
    }

        /**
         * 点击事件
         */
        holder.goodsDisplay.setOnClickListener(new View.OnClickListener() {
            /**
             * 点击每个RecyclerView子组件进行相应的响应事件,点击跳转至编辑界面
             * @Author: 李柏睿
             * @Time: 2023/4/28
             */
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,position);
            }
        });
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
        ImageView goodItemImg;
        TextView goodItemTitle;
        TextView goodsItemPrice;
        RoundedImageView goodItemUserImg;
        TextView goodItemUsername;
        LinearLayout goodsDisplay;

        public ViewHolder(View itemView) {
            super(itemView);
            goodItemImg=itemView.findViewById(R.id.good_item_img);
            goodItemTitle=itemView.findViewById(R.id.good_item_title);
            goodsItemPrice=itemView.findViewById(R.id.goods_item_price);
            goodItemUserImg=itemView.findViewById(R.id.good_item_user_img);
            goodItemUsername=itemView.findViewById(R.id.good_item_username);
            goodsDisplay=itemView.findViewById(R.id.goods_display);

        }
    }

    /**
     * 设置item的间距
     * @author carollkarry
     * @time 2023/4/22
     */
    public static class spaceItem extends RecyclerView.ItemDecoration{
        //设置item的间距
        private int space=5;
        public spaceItem(int space){
            this.space=space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            outRect.bottom=space;
            outRect.top=space;
        }
    }

    /**
     * item点击响应函数
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/4/28
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @Author: 李柏睿
     * @Time: 2023/4/28
     */
    public interface OnItemClickListener {
        /**参数（父组件，当前单击的View,单击的View的位置，数据）*/
        public void onItemClick(View view, int position);
    }
    /**
     * 消息发送接收，异步更新UI
     * @Author: 李柏睿
     * @Time: 2023/4/28
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 获取地址信息列表
                 */
                case 1:

                    break;
                case 2:


                    break;
                /**
                 * 留言模块，留言后更新留言列表
                 */
                case 3:

                    break;
                /**
                 *  更新轮播图
                 */
                case 4:

                    break;
                default:
                    break;
            }
        }
    }


}