package com.example.cugerhuo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;

/**
 * 搜索
 * @Author: 李柏睿
 * @Time: 2023/4/11 15:29
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imageSearch=findViewById(R.id.image_search);
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
            default:
                break;
    }
}}