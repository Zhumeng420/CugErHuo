package com.example.cugerhuo.activity;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.user.AddressInfo;
import com.example.cugerhuo.access.user.CommentInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewAddressAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewCommentAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.views.InputTextMsgDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情页
 * @Author: 李柏睿
 * @Time: 2023/4/27 17:33
 */
public class GoodDetailActivity extends AppCompatActivity implements View.OnClickListener{

    /**添加评论线性布局*/
    private LinearLayout msgSend;
    /**添加评论线性布局中的textview*/
    private TextView msgText;
    /**依次为出价，评论和收藏线性布局*/
    private LinearLayout bidLayout,commentLayout,collectionLayout;
    /**收藏*/
    private ImageView collectionIcon;
    /**收藏数*/
    private TextView collectionNum;
    /**留言模块*/
    private LinearLayout commentFragment;
    private TextView commentText,commentNum;
    /**出价模块*/
    private LinearLayout bidFragment;
    private TextView bidText,bidNum;
    /**0为留言，1为出价*/
    private int switchFlag = 0;
    /**留言RecyclerView，商品推荐RecyclerView*/
    private RecyclerView commentRecyclerView,goodsRecyclerView;
    /**留言信息列表*/
    private List<CommentInfo> commentInfos =new ArrayList<>();
    /**出价信息列表*/
    private List<CommentInfo> bidInfos =new ArrayList<>();
    /**留言RecyclerView适配器*/
    private RecyclerViewCommentAdapter adapter;
    /**商品推荐RecyclerView适配器*/
    private RecyclerViewGoodsDisplayAdapter goodsAdapter;
    /**发布者其他商品信息滚动*/
    private  LinearLayout otherGoods;
    private LayoutInflater goodsInflater;
    private final GoodDetailActivity.MyHandler MyHandler =new GoodDetailActivity.MyHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        initView();
        // 开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            MyHandler.sendMessage(msg);
        }).start();
    }

    /**
     * 初始化各控件
     * @Author: 李柏睿
     * @Time: 2023/4/27 19:13
     */
    public void initView(){
        msgSend = findViewById(R.id.msg_send);
        msgSend.setOnClickListener(this);
        msgText = findViewById(R.id.msg_detail);
        SpannableString s2 = new SpannableString("留言问更多细节~");
        msgText.setHint(s2);
        bidLayout = findViewById(R.id.bid_layout);
        commentLayout = findViewById(R.id.message_layout);
        collectionLayout = findViewById(R.id.collection_layout);
        bidLayout.setOnClickListener(this);
        commentLayout.setOnClickListener(this);
        collectionLayout.setOnClickListener(this);
        collectionIcon = findViewById(R.id.collection_icon);
        collectionNum = findViewById(R.id.collection_text);
        /**留言模块*/
        commentFragment = findViewById(R.id.comment_fragment);
        commentFragment.setOnClickListener(this);
        commentText = findViewById(R.id.comment_text);
        commentNum = findViewById(R.id.comment_num);
        /**出价模块*/
        bidFragment = findViewById(R.id.bid_fragment);
        bidFragment.setOnClickListener(this);
        bidText = findViewById(R.id.bid_text);
        bidNum = findViewById(R.id.bid_num);
        /**留言RecyclerView中信息添加*/
        commentRecyclerView = findViewById(R.id.comment_recycler_view);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this){
            /**禁止RecyclerView的纵向滑动
             * 解决RecyclerView滑动无惯性问题，解决滑动显示头尾阻尼问题。*/
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        for(int i=0;i<6;i++){
            CommentInfo part= new CommentInfo();
            commentInfos.add(part);
            bidInfos.add(part);
        }
        adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,0);
        commentRecyclerView.setAdapter(adapter);
        /**推荐商品模块*/
        /**初始化adapter**/
        goodsAdapter=new RecyclerViewGoodsDisplayAdapter(getActivity());
        goodsRecyclerView = findViewById(R.id.display_good);
        /**禁止recyclerView滑动**/
        goodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclerView网格布局，即横向排两个item**/
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        /**设置每个item之间的间距**/
        goodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));
        goodsRecyclerView.setAdapter(goodsAdapter);
        /**用户其他商品信息*/
        otherGoods = findViewById(R.id.other_goods);
        goodsInflater = LayoutInflater.from(this);
        for (int i = 0; i < 10; i++) {
            View view = goodsInflater.inflate(R.layout.item_user_goods,
                    otherGoods, false);
            otherGoods.addView(view);
        }
    }

    /**
     * 点击事件
     * @Author: 李柏睿
     * @Time: 2023/4/27 17:35
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /**评论*/
            case R.id.msg_send:
                if(switchFlag==0){
                    final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(GoodDetailActivity.this, R.style.dialog_center);
                    inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                        @Override
                        public void onTextSend(String msg) {

                        }
                    });
                    inputTextMsgDialog.show();
                }else{

                }

                break;
            /**底部出价*/
            case R.id.bid_layout:
                break;
            /**底部评论*/
            case R.id.message_layout:
                break;
            /**底部收藏*/
            case R.id.collection_layout:
                collectionIcon.setImageResource(R.drawable.icon_collection_selected);
                collectionNum.setText("1");
                break;
            /**留言模块*/
            case R.id.comment_fragment:
                commentRecyclerView.removeAllViews();
                bidText.setTextAppearance(this,R.style.good_fragment_unselected);
                bidNum.setVisibility(View.GONE);
                commentText.setTextAppearance(this,R.style.good_fragment_selected);
                commentNum.setVisibility(View.VISIBLE);
                SpannableString s1 = new SpannableString("留言问更多细节~");
                msgText.setHint(s1);
                switchFlag = 0;
                adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,switchFlag);
                commentRecyclerView.setAdapter(adapter);
                break;
            /**出价模块*/
            case R.id.bid_fragment:
                commentRecyclerView.removeAllViews();
                bidText.setTextAppearance(this,R.style.good_fragment_selected);
                bidNum.setVisibility(View.VISIBLE);
                commentText.setTextAppearance(this,R.style.good_fragment_unselected);
                commentNum.setVisibility(View.GONE);
                SpannableString s2 = new SpannableString("感兴趣就出个价看看吧~");
                msgText.setHint(s2);
                switchFlag = 1;
                adapter = new RecyclerViewCommentAdapter(getActivity(), bidInfos,switchFlag);
                commentRecyclerView.setAdapter(adapter);
                break;
            default:
                break;
        }
    }

    /**
     * 消息发送接收，异步更新UI
     * @Author: 李柏睿
     * @Time: 2023/4/28
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 获取地址信息列表
                 */
                case 1:
                    /**
                     * 点击item进行跳转并传值过去
                     */
                    goodsAdapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int flag = 1;
                            Intent intent=new Intent(GoodDetailActivity.this, GoodDetailActivity.class);
                            intent.putExtra("flag",flag);
                            //startActivity(intent);
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