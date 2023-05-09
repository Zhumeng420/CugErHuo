package com.example.cugerhuo.activity.mycenter;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cugerhuo.R;
import com.example.cugerhuo.activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewEvaluateAdapter;

/**
 *商品待评价页
 * @author carollkarry
 * @time 2023/5/9
 */
public class EvaluateActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGoods;
    private RecyclerViewEvaluateAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        recyclerViewGoods=findViewById(R.id.recyclerViewGoods);
        adapter=new RecyclerViewEvaluateAdapter(EvaluateActivity.this);
        recyclerViewGoods.setLayoutManager(new LinearLayoutManager(EvaluateActivity.this));
        recyclerViewGoods.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewEvaluateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(EvaluateActivity.this,EvaluateDetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}