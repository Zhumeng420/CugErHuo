package com.example.cugerhuo.activity;

import static com.example.cugerhuo.activity.MyCenterActivity.focusNum;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewRecommenduser;
import com.example.cugerhuo.tools.LettuceBaseCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 搜索结果显示页
 * @author carollkarry
 * @time 2023/5/4
 */
public class SearchResultActivity extends AppCompatActivity {

    /**
     * commodityList 搜索到的商品列表
     * partUserInfos 搜索到的用户列表
     * commodityUser 搜索到的商品列表对应的用户列表
     * searchRecyclerView 用户recyclerView
     * searchGoodsRecyclerView 商品的recyclerView
     * adapterUser 用户列表的适配器
     * adapterGoods 商品列表的适配器
     * searchText 搜索框
     * noSearchUser 未搜到到用户时的文字提示
     * noSearchGoods 未搜索到商品时的文字提示
     * resultBtnSearch 搜索按钮
     * @author 唐小莉
     * @time 2023/5/4
     */
    private List<Commodity> commodityList=new ArrayList<>();
    private List<PartUserInfo> partUserInfos=new ArrayList<>();
    private List<PartUserInfo> commodityUser=new ArrayList<>();
    private RecyclerView searchRecyclerView;
    private RecyclerView searchGoodsRecyclerView;
    private RecyclerViewRecommenduser adapterUser;
    private RecyclerViewGoodsDisplayAdapter adapterGoods;
    private EditText searchText;
    private TextView noSearchUser;
    private TextView noSearchGoods;
    private Button resultBtnSearch;
    private final MyHandler MyHandler =new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sreach_result);
        init();
        /**
         * 从上个页面获取模糊搜索结果
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        commodityList= (List<Commodity>) bundle.getSerializable("commodityList");
        partUserInfos= (List<PartUserInfo>) bundle.getSerializable("partUserInfos");
        commodityUser= (List<PartUserInfo>) bundle.getSerializable("commodityUser");
        String text=intent.getStringExtra("searchKey");

        /**
         * 将搜索框文字设为上个页面搜索框文字
         */
        searchText.setText(text);
        /**
         * 如果商品列表没有数据，则进行提示
         */
        if(commodityList.size()==0&&partUserInfos.size()!=0){
            noSearchGoods.setVisibility(View.VISIBLE);
        }
        /**
         * 如果用户列表没有数据，进行提示
         */
        if(commodityList.size()!=0&&partUserInfos.size()==0){
            noSearchUser.setVisibility(View.VISIBLE);
        }
        /**
         * 两者都没有数据，全部进行提示
         */
        if(commodityList.size()==0&&partUserInfos.size()==0){
            noSearchGoods.setVisibility(View.VISIBLE);
            noSearchUser.setVisibility(View.VISIBLE);
        }
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        /**禁止recyclerView滑动**/
        searchGoodsRecyclerView.setNestedScrollingEnabled(false);
        /**设置recyclorView网格布局，即横向排两个item**/
        searchGoodsRecyclerView.setLayoutManager(new GridLayoutManager(SearchResultActivity.this,2));
        /**设置每个item之间的间距**/
        searchGoodsRecyclerView.addItemDecoration(new RecyclerViewGoodsDisplayAdapter.spaceItem(10));

        adapterUser=new RecyclerViewRecommenduser(SearchResultActivity.this,partUserInfos);
        adapterGoods=new RecyclerViewGoodsDisplayAdapter(SearchResultActivity.this,commodityList,commodityUser);
        searchRecyclerView.setAdapter(adapterUser);
        searchGoodsRecyclerView.setAdapter(adapterGoods);

        /**
         * 点击用户列表进行跳转
         * @author 唐小莉
         * @time 2023/5/4
         */
        adapterUser.setOnItemUserClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(), OtherPeopleActivity.class);
                intent.putExtra("concernUser",partUserInfos.get(position));
                intent.putExtra("focusNum",focusNum);
                //startActivity(intent);
                startActivityForResult(intent,3);
            }
        });

        /**
         * 点击item进行跳转并传值过去
         */
        adapterGoods.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int flag = 1;
                Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                intent.putExtra("commodity",commodityList.get(position));
                intent.putExtra("user",commodityUser.get(position));
                //startActivity(intent);
                startActivityForResult(intent,1);
            }
        });

        resultBtnSearch.setOnClickListener(this::searchClick);


    }

    /**
     * 初始化控件
     * @author 唐小莉
     * @time 2023/5/4
     */
    public void init(){
        searchRecyclerView=findViewById(R.id.search_user);
        searchGoodsRecyclerView=findViewById(R.id.search_goods);
        searchText=findViewById(R.id.result_search_text);
        noSearchGoods=findViewById(R.id.no_search_goods);
        noSearchUser=findViewById(R.id.no_search_user);
        resultBtnSearch=findViewById(R.id.result_btn_search);
    }

    /**
     * 搜索按钮点击事件
     * @param view
     * @author 唐小莉
     * @time 2023/5/4
     */
    public void searchClick(View view){
        new Thread(()->{
            Message msg = Message.obtain();
            msg.arg1 = 1;
            /**
             * 搜索框不为空，则进行模糊搜索数据获取
             */
            if(!"".equals(searchText.getText().toString())){
                commodityList= CommodityOperate.getSearchCommodity(searchText.getText().toString(),getActivity());
                partUserInfos= UserOperate.getSearchUser(searchText.getText().toString(),getActivity());
            }
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
            for(int i=0;i<commodityList.size();i++){

                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,commodityList.get(i).getUserId(),SearchResultActivity.this);
                commodityUser.add(part);
            }
            MyHandler.sendMessage(msg);
        }).start();
    }
    /**
     * 消息发送接收，异步更新UI
     * @Author: 唐小莉
     * @Time: 2023/5/4
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 1:
                    noSearchGoods.setVisibility(View.GONE);
                    noSearchUser.setVisibility(View.GONE);
                    if(commodityList.size()==0&&partUserInfos.size()!=0){
                        noSearchGoods.setVisibility(View.VISIBLE);
                    }
                    if(commodityList.size()!=0&&partUserInfos.size()==0){
                        noSearchUser.setVisibility(View.VISIBLE);
                    }
                    if(commodityList.size()==0&&partUserInfos.size()==0){
                        noSearchGoods.setVisibility(View.VISIBLE);
                        noSearchUser.setVisibility(View.VISIBLE);
                    }
                    adapterUser=new RecyclerViewRecommenduser(SearchResultActivity.this,partUserInfos);
                    adapterGoods=new RecyclerViewGoodsDisplayAdapter(SearchResultActivity.this,commodityList,commodityUser);
                    searchRecyclerView.setAdapter(adapterUser);
                    searchGoodsRecyclerView.setAdapter(adapterGoods);
                    /**
                     * 点击用户列表进行跳转
                     * @author 唐小莉
                     * @time 2023/5/4
                     */
                    adapterUser.setOnItemUserClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(getActivity(), OtherPeopleActivity.class);
                            intent.putExtra("concernUser",partUserInfos.get(position));
                            intent.putExtra("focusNum",focusNum);
                            //startActivity(intent);
                            startActivityForResult(intent,3);
                        }
                    });

                    /**
                     * 点击item进行跳转并传值过去
                     */
                    adapterGoods.setOnItemClickListener(new RecyclerViewGoodsDisplayAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            int flag = 1;
                            Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                            intent.putExtra("commodity",commodityList.get(position));
                            intent.putExtra("user",commodityUser.get(position));
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