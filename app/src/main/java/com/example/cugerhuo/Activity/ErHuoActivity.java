package com.example.cugerhuo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cugerhuo.R;

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
 */

/**
 * "贰货“主页
 */
public class ErHuoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo);
    }
}