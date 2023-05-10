package com.example.cugerhuo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.GoodDetailActivity;
import com.example.cugerhuo.activity.adapter.RecycleViewMyCollectsAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewOnSellAdapter;

import java.util.List;

/**
 * 他人商品页
 * @Author: 李柏睿
 * @Time: 2023/5/10 19:17
 */
public class ProductFragment extends Fragment {
    /**
     * title 标题
     **/
    private String title;
    private RecyclerView recyclerViewOnSell;
    private RecyclerViewOnSellAdapter adapter;
    private List<Commodity> a;
    private PartUserInfo b;
    /**商品recycleview*/
    private RecyclerView recyclerViewProducts;
    /**商品recycleview适配器*/
    private RecycleViewMyCollectsAdapter productsAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public ProductFragment(String t,List<Commodity>a,PartUserInfo b){
        super();
        this.title=t;
        this.a=a;
        this.b=b;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_fragment_product, container, false);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewOtherSells);
        productsAdapter=new RecycleViewMyCollectsAdapter(getActivity(),a);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewProducts.setAdapter(productsAdapter);
        /**禁止recyclerView滑动**/
        recyclerViewProducts.setNestedScrollingEnabled(false);
        productsAdapter.setOnItemClickListener(new RecycleViewMyCollectsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(), GoodDetailActivity.class);
                intent.putExtra("commodity",a.get(position));
                intent.putExtra("user",b);
                startActivity(intent);
                startActivityForResult(intent,1);
            }
        });
        return view;
    }
}
