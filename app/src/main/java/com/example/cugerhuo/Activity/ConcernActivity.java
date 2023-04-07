package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cugerhuo.Activity.adapter.Concern;
import com.example.cugerhuo.Activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.R;

/**
 * 关注列表
 * @author 唐小莉
 * @Time 2023/4/4 21:02
 */
public class ConcernActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);

        //concern是自定义的存储关注列表的用户信息的类
        Concern concern=new Concern("RecyclerView2-","xxxx","你好");
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter adapter2 = new RecyclerViewAdapter(this,concern);
        recyclerView2.setAdapter(adapter2);

    }
}