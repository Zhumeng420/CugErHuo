package com.example.cugerhuo.fragment;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.ConcernCommodity;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.commodity.RecommendInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.access.util.MsgEvent1;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.SearchResultActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewRecommenduser;
import com.example.cugerhuo.tools.LettuceBaseCase;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

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
     * 关注的id列表
     */
    private List<Integer> concern=new ArrayList<>();
    /**
     * 商品的用户
     */
    private List<PartUserInfo>userInfos=new ArrayList<>();
    private final MyHandler MyHandler =new MyHandler();

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
        new Thread(()->{
            Message msg = Message.obtain();
            msg.arg1 = 1;
            /**
             * 获取关注列表
             */
            concern= UserOperate.getConcernId(UserInfo.getid(),getActivity());

            ConcernCommodity concernCommodity=new ConcernCommodity();
            concernCommodity.setPage(1);
            concernCommodity.setUsers(concern);
            /**
             * 获取关注的人的商品列表
             */
            commodities= CommodityOperate.getConcernCommodity(concernCommodity,getActivity());
            /**
             * 建立连接对象
             */
            LettuceBaseCase lettuce=new LettuceBaseCase();
            /**
             * 获取连接
             */
            RedisCommands<String, String> con=lettuce.getSyncConnection();

            /**
             * 通过连接调用查询
             */
            for(int i=0;i<commodities.size();i++){

                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,commodities.get(i).getUserId(), getActivity());
                userInfos.add(part);
            }
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
     * @Author: 唐小莉
     * @Time: 2023/5/5
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    adapter=new RecyclerViewGoodsDisplayAdapter(getActivity(),commodities,userInfos);
                    goodsRecyclerView.setAdapter(adapter);
                    /**
                     * 点击进行跳转
                     */
                    adapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                            intent.putExtra("commodity",commodities.get(position));
                            intent.putExtra("user",userInfos.get(position));
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
