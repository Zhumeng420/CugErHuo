package com.example.cugerhuo.Fragment;

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
import com.example.cugerhuo.activity.AddressManageActivity;
import com.example.cugerhuo.activity.EditAddressActivity;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewAddressAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;

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
     * 构造函数
     * @param title
     */
    public SuggestFragment(String title){
        super();
        this.title=title;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);
        /**初始化adapter**/
        adapter=new RecyclerViewGoodsDisplayAdapter(this.getContext());
        goodsRecyclerView =view.findViewById(R.id.display_good_block);
        /**禁止recyclerView滑动**/
        goodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclerView网格布局，即横向排两个item**/
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        /**设置每个item之间的间距**/
        goodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));
        goodsRecyclerView.setAdapter(adapter);
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
                    /**
                     * 点击item进行跳转并传值过去
                     */
                    adapter.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int flag = 1;
                            Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
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
