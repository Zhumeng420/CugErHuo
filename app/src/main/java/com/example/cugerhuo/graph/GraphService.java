package com.example.cugerhuo.graph;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.baidu.aip.imagesearch.AipImageSearch;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.tools.MyToast;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * 图像搜索服务单例模式
 *  * @author 施立豪
 *  * @time 2023/4/29
 */
public class GraphService {
    private static GraphService instance = null;
    AipImageSearch graphSearch=null;
    /**
     *  让构造函数为 private，这样该类就不会被实例化
     */
    private GraphService(Context context)
    {
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将进行自动登录）。不能对初始化语句添加进程判断逻辑。
        NIMClient.init(context,  null, null);
        // ... your codes
        LoginInfo info = new LoginInfo("cugerhuo"+ UserInfo.getid(),"123456");
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
//                        MyToast.toast(ErHuoActivity.this,"登录成功",3);
                        System.out.println("success1");
//                        Intent intent = new Intent(context, MyService.class);
//                        context.startService(intent);
                        // your code
                        /**
                         *监听数据同步状态
                         */
                        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
                            @Override
                            public void onEvent(LoginSyncStatus status) {
                                if (status == LoginSyncStatus.BEGIN_SYNC) {
                                    //MyToast.toast(ChatActivity.this,"数据同步开始",3);
                                } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
                                    //MyToast.toast(ChatActivity.this,"数据同步完成",3);
                                }
                            }
                        }, true);
                    }
                    @Override
                    public void onFailed(int code) {
                        System.out.println("fails11");
                        if (code == 302) {
                            MyToast.toast(context,"账号密码错误",1);
                            // your code
                        } else {
                            // your code
                        }
                    }
                    @Override
                    public void onException(Throwable exception) {
                        // your code
                    }
                };
        /**执行手动登录*/
        NIMClient.getService(AuthService.class).login(info).setCallback(callback);

        /**
         * 监听登录状态
         */
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode> () {
                    @Override
                    public void onEvent(StatusCode status) {
                        //获取状态的描述
                        String desc = status.getDesc();
                        if (status.wontAutoLogin()) {
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                        }
                    }
                }, true);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                System.out.println("fromgraph");
                // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                String content = imMessages.get(0).getContent();//消息
                String fromAccount = imMessages.get(0).getFromAccount();//发送消息的好友
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("channel_id", "channel_name",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("channel_description");
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[] {100, 200, 300, 400, 500});

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
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
    }
    /**
     *  获取唯一可用的对象
     */
    public static GraphService getInstance(Context context){
        if(instance==null)
        {
            instance=new GraphService(context);
        }
        return instance;
    }
    public static AipImageSearch getGraphSearchClient()
    {
        return instance.graphSearch;
    }

}
