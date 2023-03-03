package com.example.cugerhuo.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cugerhuo.R;

/**
 * 灵动岛消息提醒
 * @author 朱萌
 * @time 2023/03/03 13：16
 */
public class toast {
    /**
     *
     * @param context  传入当前的activity
     * @param messgage  需要提醒的消息
     * @param i 需要提醒的类型
     * i=0 警告提醒；i=1 错误提醒；i=2 加载提醒；i=3 成功提醒
     */
    public static  void Toast(Context context,String messgage,int i){
        android.widget.Toast toast2=new android.widget.Toast(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        /**
         * 自定义toast布局
         * ImaageView显示类型图标
         * TextView显示提醒的消息内容
         */
        View view=inflater.inflate(R.layout.toast, null);
        ImageView imageview= (ImageView) view.findViewById(R.id.tip_img);
        TextView textview= (TextView) view.findViewById(R.id.tip_info);
        /**
         * 根据不同的参数选择加载不同的状态图标
         */
        if(i==0){
            imageview.setImageResource(R.drawable.warn);
        }else if(i==1){
            imageview.setImageResource(R.drawable.error);
        }else if(i==2){
            imageview.setImageResource(R.drawable.load);
        }else if(i==3){
            imageview.setImageResource(R.drawable.sucess);
        }
        /**
         * 设置toast显示的消息文字
         */
        textview.setText(messgage);
        /**
         * toast显示的位置和时间长短
         */
        toast2.setView(view);//将默认的toast布局更改为view布局
        toast2.setGravity(Gravity.TOP, 0, 50);//toast显示的位置
        toast2.setDuration(android.widget.Toast.LENGTH_SHORT);//显示的时间长短
        toast2.show();
    }
}
