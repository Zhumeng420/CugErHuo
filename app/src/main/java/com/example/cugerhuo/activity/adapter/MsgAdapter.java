package com.example.cugerhuo.activity.adapter;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.hanks.htextview.base.DisplayUtils.getScreenWidth;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;
import static com.netease.nim.highavailable.HighAvailableObject.getContext;
import static com.tencent.beacon.event.UserAction.mContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.Msg;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.CreatTradeActivity;
import com.example.cugerhuo.activity.LocationDetailActivity;
import com.example.cugerhuo.activity.LocationViewActivity;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.oss.InitOS;
import com.example.cugerhuo.tools.entity.TradeInfo;
import com.luck.picture.lib.interfaces.OnItemClickListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
    private OnItemClickListener mTradeClickListener;
    private OnItemClickListener mTradeDetailClickListener;
    private OnItemClickListener mImgClickListener;
    private MediaPlayer mediaPlayer;

    public MsgAdapter(List<Msg> list, PartUserInfo chatUser,Context context){
        this.list = list;
        this.chatUser=chatUser;
        this.context=context;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        /**左侧收到消息*/
        LinearLayout leftLayout;
        TextView left_msg;
        /**右侧发出消息*/
        LinearLayout rightLayout;
        TextView right_msg;
        /**发消息中的头像*/
        RoundedImageView incomingAvatar;
        RoundedImageView outcomingAvatar;
        /**左侧确认订单*/
        LinearLayout leftCard;
        RoundedImageView incomingAvatarCard;
        LinearLayout leftCardDetail;
        /**右侧确认订单*/
        LinearLayout rightCard;
        RoundedImageView outcomingAvatarCard;
        LinearLayout rightCardDetail;
        /**对方已提交订单*/
        LinearLayout confirmCard;
        /**点击待确认订单*/
        private RecyclerViewAdapter.OnItemClickListener mTradeListener;// 声明自定义的接口
        /**点击查看订单*/
        private RecyclerViewAdapter.OnItemClickListener mTradeDetailListener;
        /**点击查看图片大图*/
        private RecyclerViewAdapter.OnItemClickListener mImgListener;

        /**左侧图片*/
        LinearLayout leftPic;
        RoundedImageView incomingAvatarPic;
        ImageView leftImg;
        /**右侧图片*/
        LinearLayout rightPic;
        RoundedImageView outcomingAvatarPic;
        ImageView rightImg;

        /**左侧语音*/
        LinearLayout leftAudio;
        RoundedImageView incomingAvatarAudio;
        TextView leftAudioText;
        LinearLayout leftClickAudio;
        ImageView receiveIvAudio;
        /**右侧语音*/
        LinearLayout rightAudio;
        RoundedImageView outcomingAvatarAudio;
        TextView rightAudioText;
        LinearLayout rightClickAudio;
        ImageView sendIvAudio;

        /**左侧地理信息*/
        LinearLayout leftLocation;
        RoundedImageView incomingAvatarLocation;
        TextView leftTextDetail;
        TextView leftTextProvince;
        LinearLayout leftClickLocation;
        ImageView receiveMap;
        /**右侧地理信息*/
        LinearLayout rightLocation;
        RoundedImageView outcomingAvatarLocation;
        TextView rightTextDetail;
        TextView rightTextProvince;
        LinearLayout rightClickLocation;
        ImageView sendMap;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);
            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
            incomingAvatar=view.findViewById(R.id.incoming_avatar);
            outcomingAvatar=view.findViewById(R.id.outcoming_avatar);
            leftCard=view.findViewById(R.id.left_card);
            rightCard=view.findViewById(R.id.right_card);
            confirmCard=view.findViewById(R.id.trade_confirm_card);
            incomingAvatarCard=view.findViewById(R.id.incoming_avatar_card);
            outcomingAvatarCard=view.findViewById(R.id.outcoming_avatar_card);
            leftCardDetail = view.findViewById(R.id.left_trade_detail);
            rightCardDetail = view.findViewById(R.id.right_trade_detail);
            leftPic = view.findViewById(R.id.left_pic);
            rightPic = view.findViewById(R.id.right_pic);
            incomingAvatarPic = view.findViewById(R.id.incoming_avatar_pic);
            outcomingAvatarPic = view.findViewById(R.id.outcoming_avatar_pic);
            leftImg = view.findViewById(R.id.received_pic);
            rightImg = view.findViewById(R.id.send_pic);
            leftAudio = view.findViewById(R.id.left_audio);
            rightAudio = view.findViewById(R.id.right_audio);
            incomingAvatarAudio = view.findViewById(R.id.incoming_avatar_audio);
            outcomingAvatarAudio = view.findViewById(R.id.outcoming_avatar_audio);
            leftAudioText = view.findViewById(R.id.receive_audio_text);
            rightAudioText = view.findViewById(R.id.send_audio_text);
            leftClickAudio = view.findViewById(R.id.receive_rlAudio);
            rightClickAudio = view.findViewById(R.id.send_rlAudio);
            receiveIvAudio = view.findViewById(R.id.receive_ivAudio);
            sendIvAudio = view.findViewById(R.id.send_ivAudio);
            leftLocation = view.findViewById(R.id.left_location);
            rightLocation = view.findViewById(R.id.right_location);
            incomingAvatarLocation = view.findViewById(R.id.incoming_avatar_location);
            outcomingAvatarLocation = view.findViewById(R.id.outcoming_avatar_location);
            leftTextDetail = view.findViewById(R.id.receive_location_detail);
            rightTextDetail = view.findViewById(R.id.send_location_detail);
            leftTextProvince = view.findViewById(R.id.receive_location_not_detail);
            rightTextProvince = view.findViewById(R.id.send_location_not_detail);
            leftClickLocation = view.findViewById(R.id.receive_map);
            rightClickLocation = view.findViewById(R.id.map_send);
            sendMap = view.findViewById(R.id.mapview_send);
            receiveMap = view.findViewById(R.id.mapview_receive);
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
        switch (msg.getType()){
            /**TYPE_RECEIVED 表示收到的消息*/
            case Msg.TYPE_RECEIVED:
                /**如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏*/
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.left_msg.setText(msg.getContent());
                /**注意此处隐藏右面的消息布局用的是 View.GONE*/
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
                {
                    holder.incomingAvatar.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
                }
                break;
            /**TYPE_SEND 表示发送的消息*/
            case Msg.TYPE_SEND:
                /**如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏*/
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.right_msg.setText(msg.getContent());
                /**同样使用View.GONE*/
                holder.leftLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(UserInfo.getUrl())&&UserInfo.getUrl()!=null)
                {
                    holder.outcomingAvatar.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
                }
                break;
            /**TYPE_RECEIVED_CARD 表示对方已确认交易信息*/
            case Msg.TYPE_RECEIVED_CARD:
                holder.leftCard.setVisibility(View.VISIBLE);
                /**同样使用View.GONE*/
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                TextView priceView1=holder.leftCard.findViewById(R.id.leftPrice);
                TextView dateView1=holder.leftCard.findViewById(R.id.leftTime);
                TextView placeView1=holder.leftCard.findViewById(R.id.leftPlace);

                String tradeString=list.get(position).getContent();
                TradeInfo tradeInfo= JSON.parseObject(tradeString, TradeInfo.class);
                if(tradeInfo!=null){
                    priceView1.setText(String.valueOf(tradeInfo.getPrice()));
                    dateView1.setText(tradeInfo.getTradeTime());
                    placeView1.setText(tradeInfo.getTradePlace());
                }
                if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
                {
                    holder.incomingAvatarCard.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
                }
                /**点击查看待确认订单*/
                holder.leftCardDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTradeDetailClickListener.onItemClick(v,position);
                    }
                });
                break;
            /**TYPE_SEND_CARD 表示我已确认交易信息*/
            case Msg.TYPE_SEND_CARD:
                holder.rightCard.setVisibility(View.VISIBLE);
                /**同样使用View.GONE*/
                holder.leftLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);

                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                TextView priceView=holder.rightCard.findViewById(R.id.pushPrice);
                TextView dateView=holder.rightCard.findViewById(R.id.pushDate);
                TextView placeView=holder.rightCard.findViewById(R.id.pushPlace);

                String tradeString1=list.get(position).getContent();
                TradeInfo tradeInfo1= JSON.parseObject(tradeString1, TradeInfo.class);
                if(tradeInfo1!=null){
                    priceView.setText(String.valueOf(tradeInfo1.getPrice()));
                    dateView.setText(tradeInfo1.getTradeTime());
                    placeView.setText(tradeInfo1.getTradePlace());
                }

                if (!"".equals(UserInfo.getUrl())&&UserInfo.getUrl()!=null)
                {
                    holder.outcomingAvatarCard.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
                }
                /**点击查看待确认订单*/
                holder.rightCardDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTradeDetailClickListener.onItemClick(v,position);
                    }
                });
                break;
            /**TYPE_CONFIRM_CARD 表示接受到对方的提交信息*/
            case Msg.TYPE_CONFIRM_CARD:
                holder.confirmCard.setVisibility(View.VISIBLE);
                /**同样使用View.GONE*/
                holder.leftLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                /**点击待确认订单*/
                holder.confirmCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTradeClickListener.onItemClick(v,position);
                    }
                });
                break;
            case Msg.TYPE_SEND_PIC:
                holder.rightPic.setVisibility(View.VISIBLE);
                Glide.with(context).load(msg.getContent()).into(holder.rightImg);
                holder.rightImg.setDrawingCacheEnabled(true);
                Log.e("TAG", "picImage " + msg.getContent());
                /**同样使用View.GONE*/
                holder.leftLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(UserInfo.getUrl())&&UserInfo.getUrl()!=null)
                {
                    holder.outcomingAvatarPic.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
                }
                break;
            case Msg.TYPE_RECEIVED_PIC:
                holder.leftPic.setVisibility(View.VISIBLE);
                Glide.with(context).load(msg.getContent()).into(holder.leftImg);
                holder.leftImg.setDrawingCacheEnabled(true);
                /**同样使用View.GONE*/
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
                {
                    holder.incomingAvatarPic.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
                }
                break;

            case Msg.TYPE_SEND_AUDIO:
                holder.rightAudio.setVisibility(View.VISIBLE);
                String[] content = msg.getContent().split(",and time is");
                holder.rightAudioText.setText(content[1]+"'");
                /**同样使用View.GONE*/
                holder.leftLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(UserInfo.getUrl())&&UserInfo.getUrl()!=null)
                {
                    holder.outcomingAvatarAudio.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
                }
                holder.rightClickAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.sendIvAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
                        AnimationDrawable  drawable = (AnimationDrawable) holder.sendIvAudio.getBackground();
                        drawable.start();
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(content[0]);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Log.d("tag", "播放完毕");
                                    drawable.stop();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case Msg.TYPE_RECEIVED_AUDIO:
                holder.leftAudio.setVisibility(View.VISIBLE);
                String[] content2 = msg.getContent().split(",and time is");
                holder.leftAudioText.setText(content2[1]+"'");
                /**同样使用View.GONE*/
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
                {
                    holder.incomingAvatarAudio.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
                }
                holder.leftClickAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.receiveIvAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
                        mediaPlayer = new MediaPlayer();
                        AnimationDrawable  drawable = (AnimationDrawable) holder.receiveIvAudio.getBackground();
                        drawable.start();
                        try {
                            mediaPlayer.setDataSource(content2[0]);
                            mediaPlayer.prepare();
                            mediaPlayer.setVolume(0.5f, 0.5f);
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Log.d("tag", "播放完毕");
                                    drawable.stop();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case Msg.TYPE_SEND_LOCATION:
                holder.rightLocation.setVisibility(View.VISIBLE);
                String[] locationSend = msg.getContent().split("and");
                String[] locationDetail = locationSend[1].split("区");
                holder.rightTextDetail.setText(locationDetail[1]);
                holder.rightTextProvince.setText(locationDetail[0]+"区");
                String url = "https://restapi.amap.com/v3/staticmap?location="+locationSend[0]+"&zoom=15&size=1200*400&markers=mid,,A:"+locationSend[0]+"&key=0fba021d7503da0fe875a5aec8fc24a8";
                Glide.with(context).load(url).into(holder.sendMap);
                holder.sendMap.setDrawingCacheEnabled(true);
                /**同样使用View.GONE*/
                holder.leftLayout.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftLocation.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                if (!"".equals(UserInfo.getUrl())&&UserInfo.getUrl()!=null)
                {
                    holder.outcomingAvatarLocation.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
                }
                holder.rightClickLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), LocationViewActivity.class);
                        intent.putExtra("latlng",locationSend[0]);
                        intent.putExtra("poi",locationSend[1]);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case Msg.TYPE_RECEIVED_LOCATION:
                holder.leftLocation.setVisibility(View.VISIBLE);

                String[] locationReceive = msg.getContent().split("and");
                String[] locationDetailTwo = locationReceive[1].split("区");
                holder.leftTextDetail.setText(locationDetailTwo[1]);
                holder.leftTextProvince.setText(locationDetailTwo[0]+"区");
                String urlReceive = "https://restapi.amap.com/v3/staticmap?location="+locationReceive[0]+"&zoom=15&size=1200*400&markers=mid,,A:"+locationReceive[0]+"&key=0fba021d7503da0fe875a5aec8fc24a8";
                Glide.with(context).load(urlReceive).into(holder.receiveMap);
                holder.receiveMap.setDrawingCacheEnabled(true);
                /**同样使用View.GONE*/
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightCard.setVisibility(View.GONE);
                holder.confirmCard.setVisibility(View.GONE);
                holder.leftCard.setVisibility(View.GONE);
                holder.rightPic.setVisibility(View.GONE);
                holder.leftAudio.setVisibility(View.GONE);
                holder.rightAudio.setVisibility(View.GONE);
                holder.leftPic.setVisibility(View.GONE);
                holder.rightLocation.setVisibility(View.GONE);
                if (!"".equals(chatUser.getImageUrl())&&chatUser.getImageUrl()!=null)
                {
                    holder.incomingAvatarLocation.setImageURI(Uri.fromFile(new File(chatUser.getImageUrl())));
                }
                holder.leftClickLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), LocationViewActivity.class);
                        intent.putExtra("latlng",locationReceive[0]);
                        intent.putExtra("poi",locationReceive[1]);
                        getActivity().startActivity(intent);
                    }
                });
                break;
            default:
                break;
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

    /**
     * 点击待确定订单
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/5/1
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mTradeClickListener = listener;
    }

    /**
     * 点击查看订单信息
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/5/1
     */
    public void setOnItemDetailClickListener(OnItemClickListener listener) {
        this.mTradeDetailClickListener = listener;
    }

    /**
     * 点击查看图片大图
     * @param listener
     * @Author: 李柏睿
     * @Time: 2023/5/1
     */
    public void setOnItemImgClickListener(OnItemClickListener listener) {
        this.mImgClickListener = listener;
    }


    /**
     * 定义RecyclerView选项单击事件的回调接口
     * @Author: 李柏睿
     * @Time: 2023/5/1
     */
    public interface OnItemClickListener {
        /**参数（父组件，当前单击的View,单击的View的位置，数据）*/
        public void onItemClick(View view, int position);
    }

    private void bigImageLoader(Bitmap bitmap){
        final Dialog dialog = new Dialog(getActivity());
        ImageView image = new ImageView(getContext());
        image.setImageBitmap(bitmap);
        dialog.setContentView(image);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });
    }


}