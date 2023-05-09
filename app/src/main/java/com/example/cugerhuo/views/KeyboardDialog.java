package com.example.cugerhuo.views;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Comment;
import com.example.cugerhuo.access.Pricing;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewCommentAdapter;
import com.example.cugerhuo.activity.post.PostSellActivity;

import java.util.List;
import java.util.Map;

/**
 * 弹出出价键盘
 * @Author: 李柏睿
 * @Time: 2023/5/8 22:11
 */
public class KeyboardDialog extends AppCompatDialog {
    private Context mContext;
    /**
     * 输入价格框
     */
    private EditText messagePriceView;

    /**
     * 全局
     */
    private LinearLayout keyBoardView;
    private int mLastDiff = 0;

    private TextView tv_price;
    private LinearLayout ll_price;
    private EditText et_price;
    private MyKeyBoardView keyboard_view;
    private LinearLayout ll_price_select;

    public interface OnTextSendListener {

        void onPriceSend(String msg);
    }

    private OnTextSendListener mOnTextSendListener;

    public KeyboardDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        setLayout();
    }

    private void init() {
        setContentView(R.layout.keyboard_dialog);
        keyBoardView = findViewById(R.id.keyboard);
        tv_price = findViewById(R.id.tv_price);
        ll_price = findViewById(R.id.ll_price);
        et_price = findViewById(R.id.input_price);
        keyboard_view = findViewById(R.id.keyboard_view);
        ll_price_select = findViewById(R.id.keyboard);

        /**数字键盘*/
        final KeyboardUtil keyboardUtil = new KeyboardUtil(getActivity());
        keyboardUtil.setOnOkClick(new KeyboardUtil.OnOkClick() {
            @Override
            public void onOkClick() {
                if (validate()) {
                    ll_price_select.setVisibility(View.GONE);
                    //输入的价格
                    String price = et_price.getText().toString();
                    mOnTextSendListener.onPriceSend(price);
                }
            }
        });
        keyboardUtil.setOnCancelClick(new KeyboardUtil.onCancelClick() {
            @Override
            public void onCancellClick() {
                ll_price_select.setVisibility(View.GONE);
            }
        });

        KeyboardUtil.mKeyboardView = keyboard_view;
        keyboardUtil.attachTo(et_price);
        et_price.setFocusable(true);
        et_price.setFocusableInTouchMode(true);
        et_price.requestFocus();
        ll_price_select.setVisibility(View.VISIBLE);

//        ll_price.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyboardUtil.attachTo(et_price);
//                et_price.setFocusable(true);
//                et_price.setFocusableInTouchMode(true);
//                et_price.requestFocus();
//                ll_price_select.setVisibility(View.VISIBLE);
//            }
//        });

        et_price.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.attachTo(et_price);
                return false;
            }
        });

    }

    public void setmOnPriceSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    /**
     * 是否输入判断并弹窗提醒
     *
     * @author: 李柏睿
     * @time: 2023/5/8
     */
    public boolean validate() {
        if ("".equals(et_price.getText().toString())) {
            Toast.makeText(mContext, "价格不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 键盘收起放下
     *
     * @Author: 李柏睿
     * @Time: 2023/5/8
     */
    @Override
    public void onBackPressed() {
        if (ll_price_select.getVisibility() == View.VISIBLE) {
            ll_price_select.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }

    @Override
    public void show() {
        super.show();
    }
}