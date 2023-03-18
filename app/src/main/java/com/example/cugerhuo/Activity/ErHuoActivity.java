package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cugerhuo.Activity.adapter.MyFragmentTabAdapter;
import com.example.cugerhuo.Fragment.ErHuoFragment;
import com.example.cugerhuo.Fragment.FragmentTabAdapter;
import com.example.cugerhuo.Fragment.MessageFragment;
import com.example.cugerhuo.Fragment.MyCenterFragment;
import com.example.cugerhuo.Fragment.XuanShangFragment;
import com.example.cugerhuo.Fragment.postFragment;
import com.example.cugerhuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部带悬浮球导航栏：
 * @link https://github.com/ittianyu/BottomNavigationViewEx
 * 使用Center Floating Action Button，分布对应ErHuoActivity、MessageActivity、MyCenterActivity、XuanShangActivity
 * @author 朱萌
 */

/**
 * 页面之间的过渡动画：
 * @link https://www.bilibili.com/video/BV1ha411K7MZ/?spm_id_from=333.337.search-card.all.click&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * 在res中的animation文件夹存放相关的转场xml文件
 * 包括slide_from_bottom,slide_from_left,slide_from_right,slide_from_top
 * slide_to_bottom,slide_from_to_left,slide_to_right,slide_to_top
 * zoom_in,zoom_out
 * static_animation
 * @author 朱萌
 *
 *----------------------------------------
 * 液体过渡动画（这个似乎更好看）
 * 使用github上的开源组件
 * @link https://www.bilibili.com/video/BV1mb4y197rJ/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @link https://www.bilibili.com/video/BV11r4y1P76D/?spm_id_from=333.788.recommend_more_video.-1&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 */

/**
 * item的加载动画
 * @link https://www.bilibili.com/video/BV1gT411v7gp/?spm_id_from=333.999.0.0&vd_source=813a2a8651fdca0418a5f61fd70ce06b
 * @author 朱萌
 */

/**
 * 下拉刷新组件
 * @link https://github.com/scwang90/SmartRefreshLayout
 * @author
 */

/**
 * "贰货“主页
 */
public class ErHuoActivity extends AppCompatActivity {

    /**
     * fragment 存放首页的所有侧滑页面
     *
     */
    private ArrayList<Fragment> fragments=new ArrayList<>(); //存放视图
    private List<String> mtitle=new ArrayList<>();  //存放底部标题
    private MyFragmentTabAdapter tabAdapter;

//    private ImageView ivTagOne;
//    private ImageView ivTagTwo;
//    private ImageView ivTagThree;
//    private ImageView ivTagFour;
//    private ImageView ivTagFive;
//
//    private TextView tvTagOne;
//    private TextView tvTagTwo;
//    private TextView tvTagThree;
//    private TextView tvTagFour;
//    private TextView tvTagFive;
//    private ViewPager viewPager;

    private RadioButton rbtn_erhuo;
    private RadioButton rbtn_xuanshang;
    private RadioButton rbtn_post;
    private RadioButton rbtn_message;
    private RadioButton rbtn_Mycenter;

    private LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo);
        initFragment();
        tabAdapter=new MyFragmentTabAdapter(getSupportFragmentManager(),fragments,mtitle);

        rbtn_erhuo=findViewById(R.id.rb_home);
        rbtn_xuanshang=findViewById(R.id.rb_xuanshang);
        rbtn_post=findViewById(R.id.rb_post);
        rbtn_message=findViewById(R.id.rb_message);
        rbtn_Mycenter=findViewById(R.id.rb_mycenter);
       // tabAdapter = new FragmentTabAdapter(this, fragments, R.id.fl_layout);
        initViews();



    }

    private void initViews(){
        Drawable drawable_erhuo=getResources().getDrawable(R.drawable.erhuo_drawble);
        drawable_erhuo.setBounds(0,0,50,50);
        rbtn_erhuo.setCompoundDrawables(null,drawable_erhuo,null,null);

        Drawable drawable_xuanshang=getResources().getDrawable(R.drawable.xuanshang_drawble);
        drawable_xuanshang.setBounds(0,0,50,50);
        rbtn_xuanshang.setCompoundDrawables(null,drawable_xuanshang,null,null);

        Drawable drawable_post=getResources().getDrawable(R.drawable.post_drawble);
        drawable_post.setBounds(0,0,200,200);
        rbtn_post.setCompoundDrawables(null,drawable_post,null,null);

        Drawable drawable_message=getResources().getDrawable(R.drawable.message_drawble);
        drawable_message.setBounds(0,0,50,50);
        rbtn_message.setCompoundDrawables(null,drawable_message,null,null);

        Drawable drawable_mycenter=getResources().getDrawable(R.drawable.mycenter_drawble);
        drawable_mycenter.setBounds(0,0,50,50);
        rbtn_Mycenter.setCompoundDrawables(null,drawable_mycenter,null,null);

    }


    /**
     * @author 唐小莉
     * @time 2023/3/16 20：10
     * 主页页面切换初始化函数，ErHuoFragment对应首页，XuanShangFragment对应悬赏页
     * postFragment对应发布页，messageFragment对应消息页，myCenterFragment对应个人中心页
     * 将所有页面加入到fragment列表中
     */
    private void initFragment(){
        Fragment ErHuoFragment = new ErHuoFragment();
        Fragment XuanShangFragment=new XuanShangFragment();
        Fragment postFragment=new postFragment();
        Fragment messageFragment=new MessageFragment();
        Fragment myCenterFragment= new MyCenterFragment();

        fragments.add(ErHuoFragment);
        fragments.add(XuanShangFragment);
        fragments.add(postFragment);
        fragments.add(messageFragment);
        fragments.add(myCenterFragment);

        mtitle.add("首页");
        mtitle.add("悬赏");
        mtitle.add("发布");
        mtitle.add("消息");
        mtitle.add("我的");
    }
//
//    /**
//     * 底部导航栏的点击事件
//     * @param view
//     * @author 唐小莉
//     * @time 2023/3/16 20:34
//     *
//     */
//    public void onClicked(View view) {
//        System.out.println(view.getId());
//        switch (view.getId()){
//            case R.id.first_page:
//                tabAdapter.setCurrentFragment(0);
//                break;
//            case R.id.xuanShang_page:
//                tabAdapter.setCurrentFragment(1);
//                break;
//            case R.id.post_page:
//                tabAdapter.setCurrentFragment(2);
//                break;
//            case R.id.message_page:
//                tabAdapter.setCurrentFragment(3);
//                break;
//            case R.id.myCenter_page:
//                tabAdapter.setCurrentFragment(4);
//                break;
//        }
//        initListener();
//    }

//    private void initViews() {
//        ivTagOne = findViewById(R.id.iv_tab_one);
//        ivTagTwo = findViewById(R.id.iv_tab_two);
//        ivTagThree = findViewById(R.id.iv_tab_three);
//        ivTagFour = findViewById(R.id.iv_tab_four);
//        ivTagFive = findViewById(R.id.iv_tab_five);
//
//        tvTagOne = findViewById(R.id.tv_tab_one);
//        tvTagTwo = findViewById(R.id.tv_tab_two);
//        tvTagThree = findViewById(R.id.tv_tab_three);
//        tvTagFour = findViewById(R.id.tv_tab_four);
//        tvTagFive = findViewById(R.id.tv_tab_five);
//
//        lin = findViewById(R.id.lin);
//        viewPager = findViewById(R.id.viewpager);
//        viewPager.setAdapter(tabAdapter);
//
//
//
//    }





//    protected void initListener() {
//        tabAdapter.setOnTabChangeListener(new FragmentTabAdapter.OnTabChangeListener() {
//            @Override
//            public void OnTabChanged(int index) {
//
//                tvTagOne.setTextColor(getResources().getColor(R.color.md_grey_700));
//                tvTagTwo.setTextColor(getResources().getColor(R.color.md_grey_700));
//                tvTagThree.setTextColor(getResources().getColor(R.color.md_grey_700));
//                tvTagFour.setTextColor(getResources().getColor(R.color.md_grey_700));
//                tvTagFive.setTextColor(getResources().getColor(R.color.md_grey_700));
//
//                ivTagOne.setImageResource(R.drawable.erhuo1);
//                ivTagTwo.setImageResource(R.drawable.xuanshang);
//                ivTagThree.setImageResource(R.drawable.post3);
//                ivTagFour.setImageResource(R.drawable.message);
//                ivTagFive.setImageResource(R.drawable.mycenter);
//
//                switch (index){
//                    case 0:
//                        tvTagOne.setTextColor(getResources().getColor(R.color.black));
//                        ivTagOne.setImageResource(R.drawable.erhuoselect);
//                        break;
//                    case 1:
//                        tvTagTwo.setTextColor(getResources().getColor(R.color.black));
//                        ivTagTwo.setImageResource(R.drawable.xuanshangselect);
//                        break;
//                    case 2:
//                        tvTagThree.setTextColor(getResources().getColor(R.color.black));
//                        ivTagThree.setImageResource(R.drawable.post3);
//                        break;
//                    case 3:
//                        tvTagFour.setTextColor(getResources().getColor(R.color.black));
//                        ivTagFour.setImageResource(R.drawable.messageselect);
//                        break;
//                    case 4:
//                        tvTagFive.setTextColor(getResources().getColor(R.color.black));
//                        ivTagFive.setImageResource(R.drawable.mycenterselect);
//                        break;
//                }
//
//            }
//        });
//    }

}