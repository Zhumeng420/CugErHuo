package com.example.cugerhuo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.imessage.MessageActivity;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.Objects;

import io.lettuce.core.api.sync.RedisCommands;


/**
 * 指纹验证：
 * @link https://www.bilibili.com/video/BV1664y1d7oG/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author  朱萌
 */
public class MyCenterActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author 唐小莉
     * @time 2023/3/20 16:36
     */
    private RoundedImageView userImage;
    private ImageView ivTabThree;
    private TextView userFocus;
    private TextView userFans;
    private TextView username;
    private final MyHandler MyHandler = new MyHandler();
    private LinearLayout llTabOne;
    private LinearLayout llTabTwo;
    private LinearLayout llTabFour;
    private LinearLayout llTabFive;
    private LinearLayout userConcern;
    private LinearLayout leUserFans;
    private  PartUserInfo part;
    public static int focusNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
        initView();
        userConcern.setOnClickListener(this::userConcernClick);
        leUserFans.setOnClickListener(this::userFansClick);
        userImage = findViewById(R.id.user_img);
        String imagpath = UserInfo.getUrl();
        if (!"".equals(imagpath)) {
            userImage.setImageURI(Uri.fromFile(new File(imagpath)));
        }
        new Thread(()->{
            Message msg = Message.obtain();
            msg.arg1 = 3;

            /**
             * 建立连接对象
             */
            LettuceBaseCase lettuce=new LettuceBaseCase();
            /**
             * 获取连接
             */
            RedisCommands<String, String> con=lettuce.getSyncConnection();
            part= UserInfoOperate.getInfoFromRedis(con,UserInfo.getid(),MyCenterActivity.this);
            MyHandler.sendMessage(msg);
        }).start();

        /**
         * 获取关注数量
         * @author 施立豪
         * @time 2023/3/26
         */
        // 5、开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;

            int userId=UserInfo.getid();
            /**
             * 获取关注数量
             */
            focusNum=UserOperate.getFocusNum(userId,MyCenterActivity.this);
            msg.arg2=focusNum;
            //4、发送消息
            MyHandler.sendMessage(msg);
        }).start();
        /**
         * 获取粉丝数量
         * @author 施立豪
         * @time 2023/3/26
         */
        // 5、开启线程
        new Thread(() -> {

            Message msg = Message.obtain();
            msg.arg1 = 2;
            int fansNum;
            int id1=UserInfo.getid();
            fansNum=UserOperate.getFansNum(id1,MyCenterActivity.this);
            msg.arg2=fansNum;
            //4、发送消息
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
        userFans =findViewById(R.id.user_fan);
        userFocus =findViewById(R.id.user_concern);
        userConcern =findViewById(R.id.concern);
        username=findViewById(R.id.username);
        leUserFans=findViewById(R.id.le_user_fans);
    }

    /**
     * 点击关注按钮跳转至关注界面
     * @param view
     * @author 唐小莉
     * @time 2023/4/13
     */
    public void userConcernClick(View view){
        Intent intent=new Intent(getApplicationContext(),ConcernActivity.class);
        startActivityForResult(intent,0x0002);

    }

    /**
     * 点击粉丝按钮跳转至粉丝界面
     * @param view
     * @author 唐小莉
     * @time 2023/4/25
     */
    public void userFansClick(View view){
        Intent intent=new Intent(getApplicationContext(),FansActivity.class);
        startActivityForResult(intent,0x0003);
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
     * 点击头像/用户名跳转个人信息（修改）界面
     * @param view
     * @Author: 李柏睿
     * @Time: 2023/3/20 18:47
     */
    public void onClickModify(View view){
        Intent intent = new Intent(getApplicationContext(),UserActivity.class);

        startActivityForResult(intent,0x0001);
    }

    /**
     * 点击设置图标跳转设置界面
     * @param view
     * @return: void
     * @Author: 李柏睿
     * @Time: 2023/3/21 19:38
     */
    public void onClickSetting(View view){
        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x0001)
        {
            String imagpath=UserInfo.getUrl();
            if(!"".equals(imagpath)&&imagpath!=null)
            {
                userImage.setImageURI(Uri.fromFile(new File(imagpath)));
            }
            String name=data.getStringExtra("username");
            if(!Objects.equals(name, "")){
                username.setText(name);
            }
        }
        else if(requestCode==0x0002){
            userFocus.setText(""+focusNum);
        }
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
                startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                overridePendingTransition(0,0);
                finish();
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
     * @author 施立豪
     * @time 2023/3/26
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 更新关注数
                 */
                case 1:
                    userFocus.setText(String.valueOf(msg.arg2));
                    break;
                /**
                 * 更新粉丝数
                 * @time 2023/3/27
                 */
                case 2:
                    userFans.setText(String.valueOf(msg.arg2));
                    break;
                case 3:
                    username.setText(part.getUserName());
                    break;
                default:
                    break;
            }
        }
    }


}