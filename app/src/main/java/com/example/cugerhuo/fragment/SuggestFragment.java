package com.example.cugerhuo.fragment;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.SetCommodityInfo;
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.access.rating.RatingOperate;
import com.example.cugerhuo.access.rating.RatingParam;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.util.MsgEvent1;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.tools.MyToast;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Random;

/**
 * 首页推荐页
 * @author carollkarry
 * @time 2023/4/22
 */
public class SuggestFragment extends Fragment {
    /**
     * title 标题
     * adapter 适配器
     * goodsRecyclerView 商品recycler
     */
    private String title;
    private RecyclerViewGoodsDisplayAdapter adapter;
    private RecyclerView goodsRecyclerView;
    private final SuggestFragment.MyHandler MyHandler =new SuggestFragment.MyHandler();
    /**
     * 下拉刷新
     */
    SmartRefreshLayout smartRefreshLayout;


    /**
     * 记录是否第一次进入页面，以更新商品
     */
    private static boolean isFirst=false;
    /**
     * 打开界面时间
     */
    private long mStartTime;
    /**
     * 首页展示商品
     */
    private List<Commodity> commodities;
    /**
     * 商品的用户
     */
    private List<PartUserInfo>userInfos;
    /**
     * 商品id
     */
    private int clickedProductId=0;
    private int pageNum=1;
    /**
     * 构造函数
     * @param title
     */
    public SuggestFragment(String title){
        super();
        this.title=title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 界面创建时，订阅事件， 接受消息
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 界面销毁时，取消订阅
        EventBus.getDefault().unregister(this);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);
        smartRefreshLayout=view.findViewById(R.id.refresh_parent);
        long stime2 = System.currentTimeMillis();
        System.out.println("oncreatefra"+stime2);


        /**初始化adapter**/
        goodsRecyclerView =view.findViewById(R.id.display_good_block);
        /**禁止recyclerView滑动**/
        goodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclorView网格布局，即横向排两个item**/
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        /**设置每个item之间的间距**/
        goodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));
        if(isFirst)
        {
            userInfos= RecommendInfo.getPartUserInfoList();
            commodities=RecommendInfo.getCommodityList();
            adapter=new RecyclerViewGoodsDisplayAdapter(getContext(),commodities,userInfos);

            goodsRecyclerView.setAdapter(adapter);
            /**
             * 点击item进行跳转并传值过去
             */
            adapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int flag = 1;
                    clickedProductId=commodities.get(position).getId();
                    Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                    intent.putExtra("commodity",commodities.get(position));
                    intent.putExtra("user",userInfos.get(position));
                    //startActivity(intent);
                    mStartTime = System.currentTimeMillis();
                    startActivityForResult(intent,1);
                }
            });

        }
        /**
         * 下拉 刷新整个RecyclerView
         */
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                /**
                 * 随机生成数，用于刷新的page
                 */
                Random r = new Random();
                int page=r.nextInt(4);
                /**
                 * 获取刷新后的商品信息
                 */
                if(clickedProductId==0){
                    if(pageNum<10){
                SetCommodityInfo.setInfoRefresh(UserInfo.getid(),pageNum++,getActivity());
                    }
                else
                {
                    MyToast.toast(getActivity(),"已经到底啦",0);
                }
                }
                else
                {
                    SetCommodityInfo.setInfoRecommendRefresh(UserInfo.getid(),page,getActivity());
                }
                    userInfos= RecommendInfo.getPartUserInfoList();
                    commodities=RecommendInfo.getCommodityList();
                    //adapter.notifyDataSetChanged();
                    adapter=new RecyclerViewGoodsDisplayAdapter(getContext(),commodities,userInfos);
                    goodsRecyclerView.setAdapter(adapter);
                /*重新刷新列表控件的数据*/
                smartRefreshLayout.finishRefresh(1500);
            }
        });

        // 开启线程
        new Thread(() -> {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            MyHandler.sendMessage(msg);
        }).start();
        return view;

    }

    /**
     * 获取标题
     * @return 标题
     * @author 唐小莉
     * @time 20023/4/22
     */
    public String getTitle() {
        return title;
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

                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 执行在主线程。
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     * @param msg 事件1
     */
    @Subscribe()
    public void onEventMainThread(MsgEvent1 msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long stime1 = System.currentTimeMillis();
                System.out.println("onrecevie"+stime1);
                // 在主线程上更新 UI
                String content = msg.getMsg()
                        + "\n ThreadName: " + Thread.currentThread().getName()
                        + "\n ThreadId: " + Thread.currentThread().getId();
                System.out.println("onEventMainThread(MsgEvent1 msg)收到" + content);
                userInfos= RecommendInfo.getPartUserInfoList();
                commodities=RecommendInfo.getCommodityList();
                adapter=new RecyclerViewGoodsDisplayAdapter(getContext(),commodities,userInfos);
                goodsRecyclerView.setAdapter(adapter);
                /**
                 * 点击item进行跳转并传值过去
                 */
                adapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int flag = 1;
                        clickedProductId=commodities.get(position).getId();
                        Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                        intent.putExtra("commodity",commodities.get(position));
                        intent.putExtra("user",userInfos.get(position));
                        intent.putExtra("position",position);
                        //startActivity(intent);
                        mStartTime=System.currentTimeMillis();
                        startActivityForResult(intent,1);
                    }
                });
                long stime11 = System.currentTimeMillis();
                System.out.println("onfinish"+stime11);
                isFirst=true;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            // 获取当前时间戳
            long endTime = System.currentTimeMillis();

            // 计算停留时间
            long stayTime = endTime - mStartTime;
            double score=0;
            stayTime/=1000;
            if(stayTime<1){score=0;}
            else if(stayTime<2){score=1;}
            else if(stayTime<3){score=2;}
            else if(stayTime<5){score=3;}
            else if(stayTime<8){score=4;}
            else{score=5;}
            double finalScore = score;
            new Thread(()->{
                RatingParam a=new RatingParam();
                a.setUserId(UserInfo.getid());
                a.setScore(finalScore);
                a.setProductId(clickedProductId);
                RatingOperate.insertRatingToRecommend(a,getActivity());
            }).start();
            // 在第一个 Activity 中显示停留时间
            //MyToast.toast(getActivity(),"time:"+stayTime,3);
        }
    }

}
