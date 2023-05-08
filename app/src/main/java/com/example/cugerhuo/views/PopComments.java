package com.example.cugerhuo.views;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Comment;
import com.example.cugerhuo.access.Pricing;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewCommentAdapter;

import java.util.List;
import java.util.Map;

/**
 * 更多留言弹出
 * @Author: 李柏睿
 * @Time: 2023/4/28 20:24
 */
public class PopComments extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    /**点击添加留言*/
    private LinearLayout mAddComment;
    /**底部TextView*/
    private TextView pleaseComment;
    /**全部留言展示*/
    private RecyclerView commentsRecyclerView;
    /**留言信息列表*/
    private Map.Entry<List<Comment>, List<PartUserInfo>> commentInfos ;
    private Map.Entry<List<Pricing>, List<PartUserInfo>> pricingInfos ;
    /**留言RecyclerView适配器*/
    private RecyclerViewCommentAdapter adapter;
    /**关闭当前dialog*/
    private ImageView closeDialog;
    /**留言数量**/
    private TextView numView;
    /**全局*/
    private LinearLayout popCommentsView;
    private int mLastDiff = 0;

    public PopComments(@NonNull Context context, int theme, Map.Entry<List<Comment>, List<PartUserInfo>> a,Map.Entry<List<Pricing>, List<PartUserInfo>> b) {
        super(context, theme);
        this.mContext = context;
        commentInfos=a;
        pricingInfos=b;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        setLayout();
    }

    private void init() {
        setContentView(R.layout.pop_comments);
        numView=findViewById(R.id.comments_num);
        mAddComment = findViewById(R.id.add_comment);
        pleaseComment = findViewById(R.id.please_comment);
        commentsRecyclerView = findViewById(R.id.comments_display);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        numView.setText(String.valueOf(commentInfos.getValue().size()));
        adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,pricingInfos,0);
        commentsRecyclerView.setAdapter(adapter);
        closeDialog = findViewById(R.id.close_comments);
        popCommentsView = findViewById(R.id.rl_pop_comments_view);
        mAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(mContext, R.style.dialog_center);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {

                    }
                });
                inputTextMsgDialog.show();
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        popCommentsView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//                if (heightDifference <= 0 && mLastDiff > 0) {
//                    dismiss();
//                }
//                mLastDiff = heightDifference;
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                getWindow().setGravity(Gravity.BOTTOM);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


            }
        });

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
