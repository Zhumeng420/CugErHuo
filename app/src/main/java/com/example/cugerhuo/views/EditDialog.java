package com.example.cugerhuo.views;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cugerhuo.R;

/**
 * 重写AlertDialog控件，实现输入具体交易地点
 * @Author: 李柏睿
 * @Time: 2023/4/30 23:51
 */
public class EditDialog extends AlertDialog implements View.OnClickListener {


    Context context;
    private TextView concern_cancel;
    private TextView concern_sure;
    private EditText trade_position;
    private String title;
    private String sure;
    private String cancel;
    /** 定义接口，实现确定按钮的点击回调*/
    private ConfirmListener confirmListener;

    public EditDialog(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //指定为自己的布局
        setContentView(R.layout.edit_dialog);
        concern_cancel=findViewById(R.id.concern_cancel);
        concern_sure=findViewById(R.id.concern_sure);
        trade_position=findViewById(R.id.edit_trade_position);

        concern_cancel.setOnClickListener(this);
        concern_sure.setOnClickListener(this);
        Window window = this.getWindow();
        /** 白色背景*/
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /** 保证EditText能弹出键盘*/
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        this.setCancelable(false);
    }

    /**
     * 设置对话框标题
     * @param title 标题
     */
    public void setTitle(String title){
        this.title=title;
    }

    /**
     * 设置确定按钮上的文字
     * @param confirmText
     */
    public void setConfirmText(String confirmText){
        this.sure=confirmText;
    }

    /**
     * 设置取消按钮上的文字
     * @param cancelText
     */
    public  void setConcernCancelText(String cancelText){
        this.cancel=cancelText;
    }

    public void setConfirmListener(ConfirmListener confirmListener){
    this.confirmListener=confirmListener;
    }

    /**
     * 确定按钮点击的监听接口
     */
    public interface ConfirmListener{

        void onConfirmClick();

    }
    @Override
    public void onClick(View view) {
        //设置弹出Dialog的相应事件
        switch (view.getId()) {
            case R.id.concern_cancel:
                this.dismiss();
                break;
            case R.id.concern_sure:
                confirmListener.onConfirmClick();
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public String getEdit(){
        return trade_position.getText().toString();
    }
}
