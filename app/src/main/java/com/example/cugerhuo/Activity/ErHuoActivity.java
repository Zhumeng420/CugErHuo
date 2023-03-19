package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.cugerhuo.Activity.adapter.MyFragmentTabAdapter;
import com.example.cugerhuo.Fragment.ErHuoFragment;
import com.example.cugerhuo.Fragment.MessageFragment;
import com.example.cugerhuo.Fragment.MyCenterFragment;
import com.example.cugerhuo.Fragment.XuanShangFragment;
import com.example.cugerhuo.Fragment.PostFragment;
import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.MyToast;

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
public class ErHuoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

    /**
     * fragment 存放首页的所有侧滑页面
     *
     */
    private ArrayList<Fragment> fragments=new ArrayList<>(); //存放视图
    private List<String> mtitle=new ArrayList<>();  //存放底部标题
    private MyFragmentTabAdapter tabAdapter;


    private RadioButton rbtn_erhuo;
    private RadioButton rbtn_xuanshang;
    private RadioButton rbtn_post;
    private RadioButton rbtn_message;
    private RadioButton rbtn_Mycenter;

    private RadioGroup radioGroup;

    private ViewPager viewPager;

    private MyToast toast=new MyToast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo);
        initFragment();



       // tabAdapter = new FragmentTabAdapter(this, fragments, R.id.fl_layout);
        initViews();
        initFragment();
        tabAdapter=new MyFragmentTabAdapter (getSupportFragmentManager());

        viewPager.setAdapter(tabAdapter);

        radioGroup.check(R.id.rb_home);
        viewPager.setCurrentItem(0,true);

        rbtn_erhuo.setOnClickListener(this::onClick);
        rbtn_xuanshang.setOnClickListener(this::onClick);
        rbtn_post.setOnClickListener(this::onClick);
        rbtn_message.setOnClickListener(this::onClick);
        rbtn_Mycenter.setOnClickListener(this::onClick);
        viewPager.addOnPageChangeListener(this);


    }
    @Override
    public void finish() {
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        viewGroup.removeAllViews();
        super.finish();
    }


    private void initViews(){
        rbtn_erhuo=findViewById(R.id.rb_home);
        rbtn_xuanshang=findViewById(R.id.rb_xuanshang);
        rbtn_post=findViewById(R.id.rb_post);
        rbtn_message=findViewById(R.id.rb_message);
        rbtn_Mycenter=findViewById(R.id.rb_mycenter);
        viewPager=findViewById(R.id.vpager);
        radioGroup=findViewById(R.id.rg_group);


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
        Fragment postFragment=new PostFragment();
        Fragment messageFragment=new MessageFragment();
        Fragment myCenterFragment= new MyCenterFragment();

        fragments.add(ErHuoFragment);
        fragments.add(XuanShangFragment);
        fragments.add(postFragment);
        fragments.add(messageFragment);
        fragments.add(myCenterFragment);
    }


    /**
     * 处理点击方法，根据参数的id区别不同的按钮，不同按钮对应不同的viewPager界面
     * viewPager的setCurrentItem方法中的第二个参数的意义是：
     *
     * 　　　　　　　　　　　　当该参数为true时，viewPager换页时是平滑的换页，会有页面移动的效果；
     *
     * 　　　　　　　　　　　　该参数为false时，viewPager换页效果没有平滑的移动，页面会直接出现。
     *
     * 　　　　　　　　　　　　该方法有一个参数的重载方法，默认有平滑换页效果。　　
     * @author 唐小莉
     * @time 2023/3/18 20:16
     * @param view
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rb_home:
                viewPager.setCurrentItem(0,true);
                toast.Toast(ErHuoActivity.this,"erhuo",3);
                break;
            case R.id.rb_xuanshang:
                viewPager.setCurrentItem(1,true);
                toast.Toast(ErHuoActivity.this,"xuanshang",3);
                break;
            case R.id.rb_post:
                viewPager.setCurrentItem(2,true);
                toast.Toast(ErHuoActivity.this,"post",3);
                break;
            case R.id.rb_message:
                viewPager.setCurrentItem(3,true);
                toast.Toast(ErHuoActivity.this,"message",3);
                break;
            case R.id.rb_mycenter:
                viewPager.setCurrentItem(4,true);
                toast.Toast(ErHuoActivity.this,"mycenter",3);
                break;
            default:
                break;

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 处理页面变化后，切换不同页面的操作
     * @param position 根据当前展示的viewPager页面，使radioGroup对应的按钮被选中
     */
    public void onPageSelected(int position){
        switch (position){
            case 0:
                radioGroup.check(R.id.rb_home);
                break;
            case 1:
                radioGroup.check(R.id.rb_xuanshang);
                break;
            case 2:
                radioGroup.check(R.id.rb_post);
                break;
            case 3:
                radioGroup.check(R.id.rb_message);
                break;
            case 4:
                radioGroup.check(R.id.rb_mycenter);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }
}