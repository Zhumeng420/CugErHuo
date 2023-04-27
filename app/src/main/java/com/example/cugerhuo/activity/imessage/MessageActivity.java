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
import com.example.cugerhuo.activity.ErHuoActivity;
import com.example.cugerhuo.activity.MyCenterActivity;
import com.example.cugerhuo.activity.PublishSelectionActivity;
import com.example.cugerhuo.activity.XuanShangActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewChatAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;

import java.util.ArrayList;
import java.util.List;

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
    /**地址信息列表*/
    private List<MessageInfo> messageInfos =new ArrayList<>();
    /**positionClick  记录目前点击的item位置*/
    private int positionClick;
    private final MessageActivity.MyHandler MyHandler =new MessageActivity.MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        chatRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        chatRecyclerView.setAdapter(recyclerViewChatAdapter);
        // 开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            MyHandler.sendMessage(msg);
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
        for(int i=0;i<20;i++){
            MessageInfo part= new MessageInfo();
            messageInfos.add(part);
        }
        recyclerViewChatAdapter=new RecyclerViewChatAdapter(getActivity(),messageInfos);
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
                /**
                 * 获取消息列表
                 */
                case 1:
                    recyclerViewChatAdapter = new RecyclerViewChatAdapter(MessageActivity.this,messageInfos);
                    chatRecyclerView.setAdapter(recyclerViewChatAdapter);
                    /**
                     * 点击item进行跳转并传值过去
                     */
                    recyclerViewChatAdapter.setOnItemClickListener(new RecyclerViewChatAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(MessageActivity.this, ChatActivity.class);
                            startActivityForResult(intent,1);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}