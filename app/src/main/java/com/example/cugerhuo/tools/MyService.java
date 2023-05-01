package com.example.cugerhuo.tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cugerhuo.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

public class MyService extends Service {

    private Observer<List<IMMessage>> incomingMessageObserver;
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("oncreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channel_description");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {100, 200, 300, 400, 500});

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

// 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.logo2)
                .setContentTitle("您收到一条新消息")
                .setContentText("messages.get(0).getContent()")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

// 发送通知
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());}
        Context context=this;
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                System.out.println("fromservice");
                // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                String content = imMessages.get(0).getContent();//消息
                String fromAccount = imMessages.get(0).getFromAccount();//发送消息的好友
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("channel_description");
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[] {100, 200, 300, 400, 500});

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

// 创建通知
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                        .setSmallIcon(R.drawable.logo2)
                        .setContentTitle("您收到一条新消息")
                        .setContentText(imMessages.get(0).getContent())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

// 发送通知
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(1, builder.build());
            }
        },true);
         incomingMessageObserver =
                messages -> {
                    if(messages.get(0).getContent()!=""){
                        // 创建通知渠道
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            channel.setDescription("channel_description");
                            channel.enableVibration(true);
                            channel.setVibrationPattern(new long[] {100, 200, 300, 400, 500});

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);
                        }

// 创建通知
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                                .setSmallIcon(R.drawable.logo2)
                                .setContentTitle("您收到一条新消息")
                                .setContentText(messages.get(0).getContent())
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);

// 发送通知
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(1, builder.build());

                    }
                };
        return START_STICKY;
    }

}