package com.example.cugerhuo.activity;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bumptech.glide.Glide;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Comment;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.Pricing;
import com.example.cugerhuo.access.comment.CommentOperate;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.pricing.PricingOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewCommentAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.activity.post.PostSellActivity;
import com.example.cugerhuo.oss.InitOS;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.views.InputTextMsgDialog;
import com.example.cugerhuo.views.KeyboardDialog;
import com.example.cugerhuo.views.PopComments;
import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 商品详情页
 * @Author: 李柏睿
 * @Time: 2023/4/27 17:33
 */
public class GoodDetailActivity extends AppCompatActivity implements View.OnClickListener, OnBannerListener {
    /**
     *当前商品
     */
    private Commodity commodity;
    /**
     * 当前用户
     */
    private PartUserInfo userInfo;
    /**
     * 推荐商品
     */
    private List<Commodity> recommendCommodities;
    private List<PartUserInfo> recommendUsersOfComs;
    /**
     * 卖家头像
     */
    private RoundedImageView sellerImage;
    /**
     * 描述框
     */
    private TextView sellerName;
    private TextView sellerDescripe;
    private TextView priceView;
    /**
     * 评论头像
     */
    private RoundedImageView myImage;
    /**
     * 卖家头像
     */
    private RoundedImageView sellerImage1;
    /**
     * 描述框
     */
    private TextView sellerName1;
    private TextView sellerDescripe1;
    private TextView goodCate;
    private TextView goodBrand;
    private TextView goodOldNew;
    private TextView goodDetail;
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
    private RecyclerView commentRecyclerView,goodsRecyclerView,pricingRecyclerView;

    /**留言信息列表*/
    Map.Entry<List<Comment>, List<PartUserInfo>> commentInfos;
    /**出价信息列表*/
    Map.Entry<List<Pricing>, List<PartUserInfo>> pricingInfos;
    /**留言RecyclerView适配器*/
    private RecyclerViewCommentAdapter adapter;
    /**商品推荐RecyclerView适配器*/
    private RecyclerViewGoodsDisplayAdapter goodsAdapter;
    /**发布者其他商品信息滚动*/
    private  LinearLayout otherGoods;

    private LayoutInflater goodsInflater;
    private final GoodDetailActivity.MyHandler MyHandler =new GoodDetailActivity.MyHandler();
    /**查看更多留言*/
    private LinearLayout lookMoreComments;
    /**卖同款和我想要*/
    private LinearLayout sellSame,iWant;
    /**轮播图*/
    private Banner banner;
    private ArrayList<String> list_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        initView();
        initData();
        // 获取推荐商品
        new Thread(() -> {
            if(commodity!=null) {
                /**
                 * 建立连接对象
                 */
                LettuceBaseCase lettuce = new LettuceBaseCase();
                /**
                 * 获取连接
                 */
                RedisCommands<String, String> con = lettuce.getSyncConnection();
                Map.Entry<List<Commodity>, List<PartUserInfo>> result = CommodityOperate.getRecommendComs(con, commodity.getId(), GoodDetailActivity.this);
                recommendCommodities = result.getKey();
                recommendUsersOfComs = result.getValue();
                commentInfos= CommentOperate.getRewards(con,commodity.getId(),GoodDetailActivity.this);
                pricingInfos= PricingOperate.getRewards(con,commodity.getId(),GoodDetailActivity.this);

            }
            Message msg = Message.obtain();
            msg.arg1 = 1;
            MyHandler.sendMessage(msg);
        }).start();
        // 获取评论数据
        new Thread(() -> {
            if(commodity!=null) {
                /**
                 * 建立连接对象
                 */
                LettuceBaseCase lettuce = new LettuceBaseCase();
                /**
                 * 获取连接
                 */
                RedisCommands<String, String> con = lettuce.getSyncConnection();
                commentInfos= CommentOperate.getRewards(con,commodity.getId(),GoodDetailActivity.this);
                pricingInfos= PricingOperate.getRewards(con,commodity.getId(),GoodDetailActivity.this);
            }
            Message msg = Message.obtain();
            msg.arg1 = 2;
            MyHandler.sendMessage(msg);
        }).start();

    }
    /**
     * 初始化商品数据和用户数据
     */
    public void initData() {
        Intent temp=getIntent();
        commodity= (Commodity) temp.getSerializableExtra("commodity");
        userInfo= (PartUserInfo) temp.getSerializableExtra("user");
        if(commodity!=null&&userInfo!=null)
        {
            sellerDescripe.setText(userInfo.getSignature());
            sellerDescripe1.setText(userInfo.getSignature());
            sellerImage1.setImageURI(Uri.fromFile(new File(getSandBoxPath(GoodDetailActivity.this)+userInfo.getImageUrl())));
            sellerImage.setImageURI(Uri.fromFile(new File(getSandBoxPath(GoodDetailActivity.this)+userInfo.getImageUrl())));
            sellerName.setText(userInfo.getUserName());
            sellerName1.setText(userInfo.getUserName());
            priceView.setText(String.valueOf(commodity.getPrice()));
            myImage.setImageURI(Uri.fromFile(new File(UserInfo.getUrl())));
            goodDetail.setText(commodity.getDescription());
            goodCate.setText(commodity.getCategory());
            goodBrand.setText(commodity.getBrand());
            /**设置轮播图*/
            setBanner();

        }
    }
    /**
     * 初始化各控件
     * @Author: 李柏睿
     * @Time: 2023/4/27 19:13
     */
    public void initView(){
        /**
         * 卖家初始化
         */
        sellerDescripe=findViewById(R.id.userDescription_top);
        sellerName=findViewById(R.id.userName_top);
        sellerImage=findViewById(R.id.userImg_top);
        sellerDescripe1=findViewById(R.id.userDescription_top);
        sellerImage1=findViewById(R.id.userImg_2);
        sellerName1=findViewById(R.id.username_2);
        myImage=findViewById(R.id.userImg);
        priceView=findViewById(R.id.detail_price);
        goodBrand=findViewById(R.id.brand_information);
        goodCate=findViewById(R.id.cate_information);
        goodOldNew=findViewById(R.id.old_new_information);
        goodDetail=findViewById(R.id.good_detail);
        recommendCommodities=new ArrayList<>();
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

        /**推荐商品模块*/

        goodsRecyclerView = findViewById(R.id.display_good);
        /**禁止recyclerView滑动**/
        goodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclerView网格布局，即横向排两个item**/
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        /**设置每个item之间的间距**/
        goodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));

        /**用户其他商品信息*/
        otherGoods = findViewById(R.id.other_goods);
        goodsInflater = LayoutInflater.from(this);
        for (int i = 0; i < 10; i++) {
            View view = goodsInflater.inflate(R.layout.item_user_goods,
                    otherGoods, false);
            otherGoods.addView(view);
        }
        /**查看更多留言*/
        lookMoreComments = findViewById(R.id.click_look_more);
        lookMoreComments.setOnClickListener(this);
        /**卖同款和我想要*/
        sellSame = findViewById(R.id.sell_same);
        sellSame.setOnClickListener(this);
        iWant = findViewById(R.id.want);
        iWant.setOnClickListener(this);
        /**轮播图*/
        banner = findViewById(R.id.banner);
    }
/**
 * 展示留言
 */
void showComment()
{
    if(switchFlag==0)
    {
        if(commentInfos==null){
            commentNum.setText("");
        }else{
            commentNum.setText(String.valueOf(commentInfos.getKey().size()));
        }
        if(commentInfos.getKey().size()>3)
        {
            List tempCom=new ArrayList<>(),tempPri=new ArrayList<>();
            for(int i=0;i<3;++i)
            {
                tempCom.add(commentInfos.getKey().get(i));
                tempPri.add(commentInfos.getValue().get(i));
            }
            Map.Entry<List<Comment>, List<PartUserInfo>> commentInfo= new AbstractMap.SimpleEntry<List<Comment>, List<PartUserInfo>>(tempCom,tempPri);
            adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfo,pricingInfos,switchFlag);
            commentRecyclerView.setAdapter(adapter);
            lookMoreComments.setVisibility(View.VISIBLE);
        }
        else
        { adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,pricingInfos,switchFlag);
            commentRecyclerView.setAdapter(adapter);
            lookMoreComments.setVisibility(View.GONE);
        }
    }
    else
    {
        if(pricingInfos==null){
            bidNum .setText("");
        }else{
            bidNum.setText(String.valueOf(pricingInfos.getKey().size()));
        }
        if(pricingInfos.getKey().size()>3)
        {
            List tempCom=new ArrayList<>(),tempPri=new ArrayList<>();

            for(int i=0;i<3;++i)
            {
                tempCom.add(pricingInfos.getKey().get(i));
                tempPri.add(pricingInfos.getValue().get(i));
            }
            Map.Entry<List<Pricing>, List<PartUserInfo>> pricingInfo= new AbstractMap.SimpleEntry<List<Pricing>, List<PartUserInfo>>(tempCom,tempPri);

            adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,pricingInfo,switchFlag);
            commentRecyclerView.setAdapter(adapter);
            lookMoreComments.setVisibility(View.VISIBLE);
        }else {
            adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,pricingInfos,switchFlag);
            commentRecyclerView.setAdapter(adapter);
            lookMoreComments.setVisibility(View.GONE);
        }
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
                            if(msg!=null&&!"".equals(msg)){
                            Date a=new Date(System.currentTimeMillis());
                            Comment temp=new Comment();
                            temp.setTime(a);
                            temp.setContent(msg);
                            temp.setUserid(UserInfo.getid());
                            temp.setCommodityid(commodity.getId());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message a=Message.obtain();
                                    try {
                                        boolean res=false;
                                        res=CommentOperate.insertComment(temp,GoodDetailActivity.this);
                                        if(res){
                                            a.arg2=1;
                                        }
                                        else{a.arg2=0;}
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    a.arg1=3;
                                    MyHandler.sendMessage(a);
                                }
                            }).start();
                            }
                            else
                            {
                                MyToast.toast(GoodDetailActivity.this,"留言不能为空",0);
                            }
                        }
                    });
                    inputTextMsgDialog.show();
                }else{
                    final KeyboardDialog keyboardDialog = new KeyboardDialog(GoodDetailActivity.this, R.style.dialog_center);
                    keyboardDialog.setmOnPriceSendListener(new KeyboardDialog.OnTextSendListener() {
                        @Override
                        public void onPriceSend(String msg) {
                            Toast.makeText(GoodDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    });
                    keyboardDialog.show();
                }
                break;
            /**底部出价*/
            case R.id.bid_layout:
                final KeyboardDialog keyboardDialog = new KeyboardDialog(GoodDetailActivity.this, R.style.dialog_center);
                keyboardDialog.setmOnPriceSendListener(new KeyboardDialog.OnTextSendListener() {
                    @Override
                    public void onPriceSend(String msg) {
                        Toast.makeText(GoodDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
                keyboardDialog.show();
                break;
            /**底部评论*/
            case R.id.message_layout:
                final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(GoodDetailActivity.this, R.style.dialog_center);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        if(msg!=null&&!"".equals(msg)){
                            Date a=new Date(System.currentTimeMillis());
                            Comment temp=new Comment();
                            temp.setTime(a);
                            temp.setContent(msg);
                            temp.setUserid(UserInfo.getid());
                            temp.setCommodityid(commodity.getId());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message a=Message.obtain();
                                    try {
                                        boolean res=false;
                                        res=CommentOperate.insertComment(temp,GoodDetailActivity.this);
                                        if(res){
                                            a.arg2=1;
                                        }
                                        else{a.arg2=0;}
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    a.arg1=3;
                                    MyHandler.sendMessage(a);
                                }
                            }).start();
                        }
                        else
                        {
                            MyToast.toast(GoodDetailActivity.this,"留言不能为空",0);
                        }
                    }
                });
                inputTextMsgDialog.show();
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
                if(commentInfos!=null)
                {
                    commentNum.setText(String.valueOf(commentInfos.getKey().size()));
                }
                switchFlag = 0;
               showComment();
                break;
            /**出价模块*/
            case R.id.bid_fragment:
                commentRecyclerView.removeAllViews();
                bidText.setTextAppearance(this,R.style.good_fragment_selected);
                bidNum.setVisibility(View.VISIBLE);
                if(pricingInfos!=null)
                {
                    bidNum.setText(String.valueOf(pricingInfos.getKey().size()));
                }
                commentText.setTextAppearance(this,R.style.good_fragment_unselected);
                commentNum.setVisibility(View.GONE);
                SpannableString s2 = new SpannableString("感兴趣就出个价看看吧~");
                msgText.setHint(s2);
                switchFlag = 1;
                adapter = new RecyclerViewCommentAdapter(getActivity(), commentInfos,pricingInfos,switchFlag);
                commentRecyclerView.setAdapter(adapter);
                showComment();
                break;
            /**查看更多留言*/
            case R.id.click_look_more:
                final PopComments popComments = new PopComments(GoodDetailActivity.this, R.style.dialog_center_comment,commentInfos,pricingInfos);
                popComments.show();
                InputMethodManager imm = (InputMethodManager) GoodDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            /**卖同款*/
            case R.id.sell_same:
                startActivity(new Intent(getApplicationContext(), PostSellActivity.class));
                overridePendingTransition(0,0);
                break;
            /**我想要*/
            case R.id.want:
                int iWant = 1;
                Intent intent=new Intent(GoodDetailActivity.this, ChatActivity.class);
                intent.putExtra("iWant",iWant);
                userInfo.setImageUrl(getSandBoxPath(GoodDetailActivity.this)+userInfo.getImageUrl());
                intent.putExtra("chatUser",userInfo);
                intent.putExtra("chatCommodity", commodity);
                startActivity(intent);
                overridePendingTransition(0, 0);
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
                    /**初始化adapter**/
                    goodsAdapter=new RecyclerViewGoodsDisplayAdapter(getActivity(),recommendCommodities,recommendUsersOfComs);
                    goodsRecyclerView.setAdapter(goodsAdapter);
                    goodsAdapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int flag = 1;
                            Intent intent=new Intent(GoodDetailActivity.this, GoodDetailActivity.class);
                            intent.putExtra("commodity",recommendCommodities.get(position));
                            intent.putExtra("user",recommendUsersOfComs.get(position));
                            intent.putExtra("flag",flag);
                            intent.putExtra("position",position);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                        }
                    });
                    break;
                case 2:


                   showComment();

                    break;
                /**
                 * 留言模块，留言后更新留言列表
                 */
                    case 3:
                    switch (msg.arg2)
                    {
                        case 1:MyToast.toast(GoodDetailActivity.this,"留言成功",3);
                            /**
                             * 刷新留言列表
                             */
                            new Thread(() -> {
                                if(commodity!=null) {
                                    /**
                                     * 建立连接对象
                                     */
                                    LettuceBaseCase lettuce = new LettuceBaseCase();
                                    /**
                                     * 获取连接
                                     */
                                    RedisCommands<String, String> con = lettuce.getSyncConnection();
                                    Map.Entry<List<Commodity>, List<PartUserInfo>> result = CommodityOperate.getRecommendComs(con, commodity.getId(), GoodDetailActivity.this);
                                    recommendCommodities = result.getKey();
                                    recommendUsersOfComs = result.getValue();
                                    commentInfos= CommentOperate.getRewards(con,commodity.getId(),GoodDetailActivity.this);
                                    pricingInfos= PricingOperate.getRewards(con,commodity.getId(),GoodDetailActivity.this);

                                }
                                Message ms = Message.obtain();
                                ms.arg1 = 2;
                                MyHandler.sendMessage(ms);
                            }).start();
                            break;
                        default:
                            MyToast.toast(GoodDetailActivity.this,"留言失败",1);
                            break;
                    }
                    break;
                /**
                 *  更新轮播图
                 */
                case 4:
                    banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                    //设置图片加载器，图片加载器在下方
                    banner.setImageLoader(new ImgLoader());
                    //设置图片网址或地址的集合
                    banner.setImages(list_path);
                    //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                    banner.setBannerAnimation(Transformer.Default);
                    //设置轮播间隔时间
                    banner.setDelayTime(3000);
                    //设置是否为自动轮播，默认是“是”
                    banner.isAutoPlay(true);
                    //设置指示器的位置，小点点，左中右。
                    banner.setIndicatorGravity(BannerConfig.CENTER)
                            //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                            .setOnBannerListener(GoodDetailActivity.this)
                            //必须最后调用的方法，启动轮播图。
                            .start();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置轮播图
     */
    private void setBanner() {
        //放图片地址的集合
        list_path = new ArrayList<>();
        String url1 = commodity.getUrl1();

        if(url1!=null&&!"".equals(url1))
        {
            String []urls=url1.split(";");
            int length=urls.length;
            String result[]=new String[length];
            result[length-1]=urls[length-1];
            // 从后往前依次减去后面一个元素
            if(length>1){
                for (int i = length - 2; i >= 0; i--) {
                    String current = urls[i];
                    String next = result[i + 1];
                    int index = current.lastIndexOf(next);
                    if(index>0){
                        result[i] = current.substring(0, index);}
                    else
                    {
                        result[i]=current;

                    }
                }
            }


        for(String url11:result){
            System.out.println("url11"+url11);
            OSSClient oss = InitOS.getOssClient();

            /**
             * 获取本地保存路径
             */
            String newUrl1 = getSandBoxPath(getActivity()) + url11;
            System.out.println("imager2"+url1);
            File f = new File(newUrl1);
            if (!f.exists()) {
                /**
                 * 构建oss请求
                 */
                GetObjectRequest get = new GetObjectRequest("cugerhuo", url11);
                /**
                 * 异步任务
                 */
                oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                    /**
                     * 下载成功
                     *
                     * @param request
                     * @param result
                     */
                    @Override
                    public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                        // 开始读取数据。
                        long length = result.getContentLength();
                        if (length > 0) {
                            byte[] buffer = new byte[(int) length];
                            int readCount = 0;
                            while (readCount < length) {
                                try {
                                    readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
                                } catch (Exception e) {
                                    OSSLog.logInfo(e.toString());
                                }
                            }
                            // 将下载后的文件存放在指定的本地路径，例如D:\\localpath\\exampleobject.jpg。
                            try {
                                FileOutputStream fout = new FileOutputStream(newUrl1);
                                fout.write(buffer);
                                fout.close();
                                /**
                                 * 下载完成，填写更新逻辑
                                 */
                                /**
                                 * 设置商品图片圆角30度
                                 */
                                System.out.println("image1"+newUrl1);
                               list_path.add(newUrl1);
                                Message msg=Message.obtain();
                                msg.arg1=4;
                                MyHandler.sendMessage(msg);
                            } catch (Exception e) {
                                OSSLog.logInfo(e.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(GetObjectRequest request, ClientException clientException,
                                          ServiceException serviceException) {
                        System.out.println("image3"+newUrl1);
                        Log.e(TAG, "oss下载文件失败");
                    }
                });
            } else {
                System.out.println("image2"+newUrl1);
                /**
                 * 设置商品图片圆角30度
                 */
                list_path.add(newUrl1);
                //设置内置样式，共有六种可以点入方法内逐一体验使用。
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                //设置图片加载器，图片加载器在下方
                banner.setImageLoader(new ImgLoader());
                //设置图片网址或地址的集合
                banner.setImages(list_path);
                //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                banner.setBannerAnimation(Transformer.Default);
                //设置轮播间隔时间
                banner.setDelayTime(3000);
                //设置是否为自动轮播，默认是“是”
                banner.isAutoPlay(true);
                //设置指示器的位置，小点点，左中右。
                banner.setIndicatorGravity(BannerConfig.CENTER)
                        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                        .setOnBannerListener(this)
                        //必须最后调用的方法，启动轮播图。
                        .start();
            }
        }
        }

    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(this, BigImgActivity.class);
        intent.putStringArrayListExtra("imgData", list_path);
        intent.putExtra("clickPosition", position);
        startActivity(intent);
    }

    //自定义的图片加载器
    private class ImgLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}