package com.example.cugerhuo.activity.imessage;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.MessageInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.ConcernActivity;
import com.example.cugerhuo.activity.ErHuoActivity;
import com.example.cugerhuo.activity.MyCenterActivity;
import com.example.cugerhuo.activity.OtherPeopleActivity;
import com.example.cugerhuo.activity.PublishSelectionActivity;
import com.example.cugerhuo.activity.XuanShangActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewChatAdapter;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 消息主页
 * 实时通信：
 * @link https://yunxin.163.com/m/im?from=bd3mjjim20220714
 * @author 朱萌
 */
public class MessageActivity extends AppCompatActivity implements View.OnClickListener{


    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author: 唐小莉
     * @time 2023/3/20 16:36
     */

    private ImageView ivTabThree;
    private LinearLayout llTabOne;
    private LinearLayout llTabTwo;
    private LinearLayout llTabFour;
    private LinearLayout llTabFive;

    private RecyclerView chatRecyclerView;
    private RecyclerViewChatAdapter recyclerViewChatAdapter;
    /**
     * messageInfos 消息信息列表，包含消息的缩略内容与时间
     * contactId 最近联系人的 ID（好友帐号)
     * chatUserInfo 最近联系人的信息
     */
    private List<MessageInfo> messageInfos =new ArrayList<>();
    private List<Integer> contactId=new ArrayList<>();
    private List<PartUserInfo> chatUserInfo =new ArrayList<>();
    /**positionClick  记录目前点击的item位置*/
    private int positionClick;
    private final MessageActivity.MyHandler MyHandler =new MessageActivity.MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        chatRecyclerView.setLayoutManager(new GridLayoutManager(this,1));

        /**
         * 获取最近会话
         * @author 唐小莉
         * @time 2023/4/30
         */
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）
                        for(int i=0;i<recents.size();i++){
                            String str="";
                            /**
                             * 截取contactId中的数字部分，作为用户id以便后续查询
                             * @author 唐小莉
                             * @time 2023/4/30
                             */
                            for(int j=0;j<recents.get(i).getContactId().length();j++){
                                if(recents.get(i).getContactId().charAt(j)>=48&&recents.get(i).getContactId().charAt(j)<=57){
                                    str+=recents.get(i).getContactId().charAt(j);
                                }
                            }
                            contactId.add(Integer.valueOf(str));
                            MessageInfo messageInfo=new MessageInfo();
                            messageInfo.setChatTime(recents.get(i).getTime());
                            messageInfo.setContent(recents.get(i).getContent());
                            messageInfos.add(messageInfo);
                        }
                    }
                });

        // 5、开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            /**
             * 建立连接对象
             */
            LettuceBaseCase lettuce=new LettuceBaseCase();
            /**
             * 获取连接
             */
            RedisCommands<String, String> con=lettuce.getSyncConnection();

            /**
             * 通过连接调用查询
             */
            for(int i=0;i<contactId.size();i++){
                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,contactId.get(i),MessageActivity.this);
                chatUserInfo.add(part);
            }
//            msg.arg2=fansNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
            /**
             * 关闭连接啊
             */
            //lettuce.close();
        }).start();


    }

    /**
     * 初始化各个控件，找到对应的组件
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void initView(){
        llTabOne =findViewById(R.id.ll_tab_one);
        llTabOne.setOnClickListener(this);
        llTabTwo =findViewById(R.id.ll_tab_two);
        llTabTwo.setOnClickListener(this);
        llTabFour =findViewById(R.id.ll_tab_four);
        llTabFour.setOnClickListener(this);
        llTabFive =findViewById(R.id.ll_tab_five);
        llTabFive.setOnClickListener(this);
        ivTabThree = (ImageView) findViewById(R.id.iv_tab_three);
        ivTabThree.setOnClickListener(this);
        chatRecyclerView=findViewById(R.id.re_chat);
    }
    /**
     * 重写finish方法，去掉出场动画
     * @author 唐小莉
     * @Time 2023/3/20 16:31
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    /**
     * 底部导航栏点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_tab_one:
                startActivity(new Intent(getApplicationContext(), ErHuoActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            /**
             * 点击悬赏图标，进行跳转到悬赏界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_two:
                startActivity(new Intent(getApplicationContext(), XuanShangActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            /**
             * 点击中间加号按钮跳转选择界面+跳转动画
             * @Author: 李柏睿
             * @Time: 2023/3/22 16:38
             */
            case R.id.iv_tab_three:
                final RotateAnimation animation = new RotateAnimation(0.0f, 90.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration( 500 );
                ivTabThree.startAnimation( animation );
                startActivity(new Intent(getApplicationContext(),PublishSelectionActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击消息图标，进行跳转到消息界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_four:
                break;
            /**
             * 点击个人中心图标，进行跳转到个人中心界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_five:
                startActivity(new Intent(getApplicationContext(), MyCenterActivity.class));
                overridePendingTransition(0,0);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 消息发送接收，异步更新UI
     * @Author: 李柏睿
     * @Time: 2023/4/22 19:48
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 1:
                    recyclerViewChatAdapter=new RecyclerViewChatAdapter(getActivity(),messageInfos,chatUserInfo);
                    chatRecyclerView.setAdapter(recyclerViewChatAdapter);
                    /**
                     * 点击进行跳转至聊天界面
                     * @author 唐小莉
                     * @time 2023/4/30
                     */
                    recyclerViewChatAdapter.setOnItemClickListener(new RecyclerViewChatAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            positionClick=position;
                            Intent intent=new Intent(getActivity(), ChatActivity.class);
                            /**
                             * 将聊天对象的信息传递过去
                             */
                            intent.putExtra("chatUser",chatUserInfo.get(position));
                            int iWant = 0;
                            intent.putExtra("iWant",iWant);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}