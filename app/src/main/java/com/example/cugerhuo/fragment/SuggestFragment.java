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
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.util.MsgEvent1;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
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
     * 首页展示商品
     */
    private List<Commodity> commodities;
    /**
     * 商品的用户
     */
    private List<PartUserInfo>userInfos;
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
                    Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                    intent.putExtra("position",position);
                    //startActivity(intent);
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
                SetCommodityInfo.setInfoRefresh(UserInfo.getid(),page,getActivity());
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
                        Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                        intent.putExtra("position",position);
                        //startActivity(intent);
                        startActivityForResult(intent,1);
                    }
                });
                isFirst=true;
            }
        });

    }

}
