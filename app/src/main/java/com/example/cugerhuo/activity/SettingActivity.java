package com.example.cugerhuo.activity;

import static com.netease.nim.highavailable.HighAvailableObject.getContext;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cugerhuo.R;
import com.example.cugerhuo.tools.MyToast;

import java.io.File;

/**
 * 设置
 * @Author: 李柏睿
 * @Time: 2023/3/22 16:40
 */
public class SettingActivity extends AppCompatActivity {
    /**
     * 清除缓存按钮
     * @author 施立豪
     * @time 2023/4/18
     */
    private TextView clearCatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        clearCatch.setOnClickListener(this::setClearCatch);
    }
    /**
     * 初始化界面
     * @author 施立豪
     * @time 2023/4/18
     */
    public void init()
    {
        clearCatch =findViewById(R.id.clearCatch);
    }
    /**
     * 清除缓存功能
     * @author 施立豪
     * @time 2023/4/18
     */
    public void setClearCatch(View view)
    {   //打开文件目录
        File file=new File(getSandboxPath());
        //当前文件是目录，删除目录下所有文件
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                //如果文件存在则删除
                if (file.exists()) {
                    file.delete();
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        }MyToast.toast(this,"清除缓存成功",3);
    }}
    /**
     * 获取缓存所在目录
     * @author 施立豪
     * @time 2023/4/18
     * @return 路径字符串
     */
    private String getSandboxPath() {
        File externalFilesDir = getContext().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }
}