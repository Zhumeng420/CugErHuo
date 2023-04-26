package com.example.cugerhuo.activity.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;

/**
 * 更改用户昵称
 * @author carollkarry
 * @time 2023/4/24
 */
public class UpdateUsernameActivity extends AppCompatActivity {
    /**
     * saveUpdateName 保存按钮
     * editUserName 输入昵称框
     */

    private TextView saveUpdateName;
    private EditText editUserName;
    private final MyHandler MyHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);
        init();
        Intent intent1 = new Intent();
        intent1.putExtra("username",String.valueOf(editUserName.getText()));
        setResult(01,intent1);
        saveUpdateName.setOnClickListener(this::click);
    }

    /**
     * 初始化控件
     * @author 唐小莉
     * @time 2023/4/26
     */
    public void init(){
        saveUpdateName=findViewById(R.id.saveUpdateName);
        editUserName=findViewById(R.id.editUserName);
    }

    /**
     * 保存按钮点击事件
     * @param view
     * @author 唐小莉
     * @time 2023/4/26
     */
    public void click(View view){
        /**
         * 如果输入不为空
         */
        if(!"".equals(String.valueOf(editUserName.getText()))){
            /**
             * 将更新后的数据返回给上个界面
             */
            Intent intent1 = new Intent();
            intent1.putExtra("username",String.valueOf(editUserName.getText()));
            setResult(01,intent1);
            new Thread(()->{
                Message msg = Message.obtain();
                msg.arg1 = 1;
                UserInfoOperate.updateUserName(UserInfo.getid(), String.valueOf(editUserName.getText()),UpdateUsernameActivity.this);
                MyHandler.sendMessage(msg);
            }).start();
            finish();
        }
        /**
         * 输入为空进行提示
         */
        else {
            Toast toast=Toast.makeText(getApplicationContext(), "更新失败，昵称不能为空！",Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * 消息发送接收，异步更新UI
     * @author 唐小莉
     * @time 2023/4/26
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 更新
                 */
                case 1:
                    /**
                     * 更新成功进行提示
                     */
                    Toast toast1=Toast.makeText(getApplicationContext(), "更新成功！",Toast.LENGTH_SHORT);
                    toast1.show();
                    break;

                default:
                    break;
            }
        }
    }
}