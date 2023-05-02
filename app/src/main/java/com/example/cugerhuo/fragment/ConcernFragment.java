package com.example.cugerhuo.fragment;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.util.MsgEvent1;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 首页关注页
 * @author carollkarry
 * @time 2023/4/22
 */
public class ConcernFragment extends Fragment {
    /**
     * title 标题
     **/
    private String title;
    private RecyclerViewGoodsDisplayAdapter adapter;
    private RecyclerView goodsRecyclerView;

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

    public ConcernFragment(String title){
        super();
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concern, container, false);

        /**初始化adapter**/
        goodsRecyclerView = view.findViewById(R.id.display_concern_good_block);
        /**禁止recyclerView滑动**/
        goodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclorView网格布局，即横向排两个item**/
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        /**设置每个item之间的间距**/
        goodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));
        if (isFirst) {
            userInfos = RecommendInfo.getPartUserInfoList();
            commodities = RecommendInfo.getCommodityList();
            adapter = new RecyclerViewGoodsDisplayAdapter(getContext(), commodities, userInfos);

            goodsRecyclerView.setAdapter(adapter);
            /**
             * 点击item进行跳转并传值过去
             */
            adapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int flag = 1;
                    Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
                    intent.putExtra("position", position);
                    //startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            });
        }


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
