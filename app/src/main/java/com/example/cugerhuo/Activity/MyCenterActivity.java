package com.example.cugerhuo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.cugerhuo.Activity.IMessageActivity.MessageActivity;
import com.example.cugerhuo.DataAccess.User.UserInfo;
import com.example.cugerhuo.DataAccess.User.UserOperate;
import com.example.cugerhuo.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;


/**
 * 指纹验证：
 * @link https://www.bilibili.com/video/BV1664y1d7oG/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author  朱萌
 */
public class MyCenterActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author: 唐小莉
     * @time 2023/3/20 16:36
     */
    private RoundedImageView user_image;
    private ImageView iv_tab_three;
    private TextView user_focus;
    private TextView user_fans;
    private mHandler mhandler = new mHandler();
    private LinearLayout ll_tab_one;
    private LinearLayout ll_tab_two;
    private LinearLayout ll_tab_four;
    private LinearLayout ll_tab_five;
    private LinearLayout user_concern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
        initView();
        user_concern.setOnClickListener(this::user_concern_click);
        user_image = findViewById(R.id.user_img);
        String imagpath = UserInfo.getUrl();
        if ("".equals(imagpath)) {
            user_image.setImageURI(Uri.fromFile(new File(imagpath)));
        }
        /**
         * 获取关注数量
         * @author 施立豪
         * @time 2023/3/26
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.arg1 = 1;
                int focusNum=0;
                /**
                 * 查询本地存储
                 * @author 施立豪
                 * @time 2023/3/27
                 */
                SharedPreferences LoginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                //获得Editor 实例
                SharedPreferences.Editor editor = LoginMessage.edit();
                String id=LoginMessage.getString("Id","");
                int Id=0;
                /**
                 * 如果当前本地没有存储id，先查询id并持久化
                 */
                if(id.equals(""))
                {

                    Id=UserOperate.GetId(LoginMessage.getString("PhoneNumber",""),MyCenterActivity.this);
                    editor.putString("Id", String.valueOf(Id));
                    editor.apply();

                }
                /**
                 * 本地有id，则查询id
                 */
                else{
                    Id=Integer.parseInt(id);
            }
                /**
                 * 获取关注数量
                 */
                focusNum=UserOperate.GetFocusNum(Id,MyCenterActivity.this);
                msg.arg2=focusNum;
                //4、发送消息
                mhandler.sendMessage(msg);
            }
            // 5、开启线程
        }).start();
        /**
         * 获取粉丝数量
         * @author 施立豪
         * @time 2023/3/26
         */
        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                msg.arg1 = 2;
                int fansNum=0;
                /**
                 * 查询本地存储
                 * @author 施立豪
                 * @time 2023/3/27
                 */
                SharedPreferences LoginMessage = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
                //获得Editor 实例
                SharedPreferences.Editor editor = LoginMessage.edit();
                String id=LoginMessage.getString("Id","");
                int Id=0;
                /**
                 * 如果当前本地没有存储id，先查询id并持久化
                 */
                if(id.equals(""))
                {

                    Id=UserOperate.GetId(LoginMessage.getString("PhoneNumber",""),MyCenterActivity.this);
                    editor.putString("Id", String.valueOf(Id));
                    editor.apply();

                }
                /**
                 * 本地有id，则查询id
                 */
                else{
                    Id=Integer.parseInt(id);

                }
                fansNum=UserOperate.GetFansNum(Id,MyCenterActivity.this);
                msg.arg2=fansNum;
                //4、发送消息
                mhandler.sendMessage(msg);
            }
            // 5、开启线程
        }).start();
    }
    /**
     * 初始化各个控件，找到对应的组件
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void initView(){
        ll_tab_one=findViewById(R.id.ll_tab_one);
        ll_tab_one.setOnClickListener(this);
        ll_tab_two=findViewById(R.id.ll_tab_two);
        ll_tab_two.setOnClickListener(this);
        ll_tab_four=findViewById(R.id.ll_tab_four);
        ll_tab_four.setOnClickListener(this);
        ll_tab_five=findViewById(R.id.ll_tab_five);
        ll_tab_five.setOnClickListener(this);
        iv_tab_three = (ImageView) findViewById(R.id.iv_tab_three);
        iv_tab_three.setOnClickListener(this);
        user_fans=findViewById(R.id.user_fan);
        user_focus=findViewById(R.id.user_concern);
        user_concern=findViewById(R.id.concern);
    }

    public void user_concern_click(View view){
        startActivity(new Intent(getApplicationContext(),ConcernActivity.class));

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
            if("".equals(imagpath))
            {
                user_image.setImageURI(Uri.fromFile(new File(imagpath)));
            }
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
                iv_tab_three.startAnimation( animation );
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
        }

    }

    /**
     * 消息发送接收，异步更新UI
     * @author 施立豪
     * @time 2023/3/26
     */
    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 更新关注数
                 */
                case 1:
                    System.out.println("关注数"+msg.arg2);
                    user_focus.setText(String.valueOf(msg.arg2));
                    break;
                /**
                 * 更新粉丝数
                 * @time 2023/3/27
                 */
                case 2:
                    System.out.println(String.valueOf(msg.arg2));

                    user_fans.setText(String.valueOf(msg.arg2));
                    break;

            }
        }
    }


}