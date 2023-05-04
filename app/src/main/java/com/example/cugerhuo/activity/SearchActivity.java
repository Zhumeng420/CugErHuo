package com.example.cugerhuo.activity;

import static com.example.cugerhuo.activity.MyCenterActivity.focusNum;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.activity.adapter.RecyclerViewCommentAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewGoodsDisplayAdapter;
import com.example.cugerhuo.tools.LettuceBaseCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * 搜索
 * @Author: 李柏睿
 * @Time: 2023/4/11 15:29
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * searchText 搜索输入框
     * btnSearch 搜索按钮
     * commodityList 搜索商品列表
     * partUserInfos 搜索用户列表
     */
    private ImageView imageSearch;
    private EditText searchText;
    private Button btnSearch;
    private List<Commodity> commodityList=new ArrayList<>();
    private List<PartUserInfo> partUserInfos=new ArrayList<>();
    private List<PartUserInfo> commodityUser=new ArrayList<>();
    private final MyHandler MyHandler =new MyHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imageSearch=findViewById(R.id.image_search);
        searchText=findViewById(R.id.search_text);
        btnSearch=findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        imageSearch.setOnClickListener(this::test);
    }
    public  void test(View view)
    {

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /**
             * 点击图像搜索跳转
             */
            case R.id.linear_searchitem:

                break;
            case R.id.btn_search:
                new Thread(()->{
                    Message msg = Message.obtain();
                    msg.arg1 = 1;
                    if(!"".equals(searchText.getText().toString())){
                        commodityList= CommodityOperate.getSearchCommodity(searchText.getText().toString(),getActivity());
                        partUserInfos= UserOperate.getSearchUser(searchText.getText().toString(),getActivity());
                    }
                    //System.out.println("gggggggggggggggggggggggggggggggggggggggg"+commodityList.get(0));
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

                        PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,commodityList.get(i).getUserId(),SearchActivity.this);
                        commodityUser.add(part);
                    }
                    MyHandler.sendMessage(msg);
                }).start();

                break;
            default:
                break;
    }
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

                    /**
                     * 点击进行跳转并传值
                     */
                    Intent intent=new Intent(getActivity(), SearchResultActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("commodityList", (Serializable) commodityList);
                    bundle.putSerializable("partUserInfos", (Serializable) partUserInfos);
                    bundle.putSerializable("commodityUser", (Serializable) commodityUser);

                    intent.putExtras(bundle);
                    intent.putExtra("searchKey",searchText.getText().toString());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}