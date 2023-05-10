package com.example.cugerhuo.activity.mycenter;

import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commerce.Commerce;
import com.example.cugerhuo.access.evaluate.EvaluationInfo;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.activity.adapter.RecyclerViewAdapter;
import com.example.cugerhuo.activity.adapter.RecyclerViewEvaluateAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *商品待评价页
 * @author carollkarry
 * @time 2023/5/9
 */
public class EvaluateActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGoods;
    private RecyclerViewEvaluateAdapter adapter;
    private List<EvaluationInfo> evaluationInfos;
    private List<PartUserInfo> evaluationUser;
    private List<Commodity> commodities=new ArrayList<>();
    private List<Commerce> commerce=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        recyclerViewGoods=findViewById(R.id.recyclerViewGoods);
        Intent intent =getIntent();
        evaluationInfos= (List<EvaluationInfo>) intent.getSerializableExtra("evaluationList");
        evaluationUser= (List<PartUserInfo>) intent.getSerializableExtra("ePartUserInfos");
        for(int i=0;i<evaluationInfos.size();i++){
            commodities.add(evaluationInfos.get(i).getCommodity());
            commerce.add(evaluationInfos.get(i).getCommerce());
        }
        adapter=new RecyclerViewEvaluateAdapter(EvaluateActivity.this,evaluationUser,commodities);
        recyclerViewGoods.setLayoutManager(new LinearLayoutManager(EvaluateActivity.this));
        recyclerViewGoods.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewEvaluateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(EvaluateActivity.this,EvaluateDetailsActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("eCommodityList", (Serializable) commodities.get(position));
                bundle1.putSerializable("commerce", (Serializable) commerce.get(position));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }
}