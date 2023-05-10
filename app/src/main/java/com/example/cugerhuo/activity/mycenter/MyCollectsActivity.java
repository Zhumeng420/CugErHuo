package com.example.cugerhuo.activity.mycenter;

import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.comment.CollectOperate;
import com.example.cugerhuo.access.comment.CommentOperate;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.pricing.PricingOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.activity.AddressManageActivity;
import com.example.cugerhuo.activity.EditAddressActivity;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecycleViewMyCollectsAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewEvaluateAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.activity.imessage.ChatActivity;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.tools.MyToast;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 我的关注界面
 * @Author: 李柏睿
 * @Time: 2023/5/10 1:50
 */
public class MyCollectsActivity extends AppCompatActivity {
    /**我的收藏id list*/
    private List<Integer> myCollects = new ArrayList<>();
    /**我的收藏*/
    private List<Commodity> commodities = new ArrayList<>();
    /**我的收藏recycleview*/
    private RecyclerView recyclerViewCollects;
    /**我的收藏recycleview适配器*/
    private RecycleViewMyCollectsAdapter adapter;

    private final MyCollectsActivity.MyHandler MyHandler =new MyCollectsActivity.MyHandler();
    private List<PartUserInfo> partUserInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collects);
        recyclerViewCollects = findViewById(R.id.recyclerMyCollects);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message a=Message.obtain();
                try {
                    boolean res=false;
                    myCollects = CollectOperate.myCollect(UserInfo.getid(), MyCollectsActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                a.arg1=1;
                MyHandler.sendMessage(a);
            }
        }).start();

    }

    /**
     * 消息发送接收，异步更新UI
     * @Author: 李柏睿
     * @Time: 2023/5/10
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){

                case 1:
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            /**
                             * 获取连接
                             */
                            for(int i=0;i<myCollects.toArray().length;i++){
                                Commodity product = CommodityOperate.getCommodity(myCollects.get(i),MyCollectsActivity.this);
                                commodities.add(product);
                                PartUserInfo user= UserInfoOperate.getInfoFromMysql(product.getUserId(),MyCollectsActivity.this);
                                partUserInfos.add(user);
                            }

                            Message a=Message.obtain();
                            a.arg1=2;
                            MyHandler.sendMessage(a);
                        }
                    }).start();
                    break;
                case 2:
                    adapter=new RecycleViewMyCollectsAdapter(MyCollectsActivity.this,commodities);
                    recyclerViewCollects.setLayoutManager(new LinearLayoutManager(MyCollectsActivity.this));
                    recyclerViewCollects.setAdapter(adapter);
                    adapter.setOnItemClickListener(new RecycleViewMyCollectsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(MyCollectsActivity.this, GoodDetailActivity.class);
                            intent.putExtra("commodity",commodities.get(position));
                            intent.putExtra("user",partUserInfos.get(position));
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