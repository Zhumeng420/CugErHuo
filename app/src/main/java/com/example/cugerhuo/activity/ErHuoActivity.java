package com.example.cugerhuo.activity;

import static android.content.ContentValues.TAG;
import static com.luck.picture.lib.config.PictureSelectionConfig.selectorStyle;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commodity.CommodityOperate;
import com.example.cugerhuo.access.user.PartUserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.activity.adapter.ViewAdapter;
import com.example.cugerhuo.activity.imessage.MessageActivity;
import com.example.cugerhuo.fragment.ConcernFragment;
import com.example.cugerhuo.fragment.SuggestFragment;
import com.example.cugerhuo.graph.GraphOperate;
import com.example.cugerhuo.graph.GraphService;
import com.example.cugerhuo.tools.LettuceBaseCase;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.tools.photoselect.GlideEngine;
import com.example.cugerhuo.tools.photoselect.ImageLoaderUtils;
import com.google.android.material.tabs.TabLayout;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.engine.CropEngine;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.engine.UriToFileTransformEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnMediaEditInterceptListener;
import com.luck.picture.lib.interfaces.OnQueryFilterListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.DateUtils;
import com.luck.picture.lib.utils.PictureFileUtils;
import com.luck.picture.lib.utils.SandboxTransformUtils;
import com.luck.picture.lib.utils.StyleUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.api.sync.RedisCommands;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;
import top.zibin.luban.OnRenameListener;

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
public class ErHuoActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 变量从one-five依次对应，首页，悬赏，发布，消息，个人中心控件
     * @author: 唐小莉
     * @time 2023/3/20 16:36
     */
    private ImageView ivTabThree;

    private LinearLayout llTabOne;
    private LinearLayout llTabTwo;
    private LinearLayout llTabFour;
    private LinearLayout llTabFive;
    private LinearLayout llSearch;
    //图片搜索
    private ImageView imageSearch;
    ViewPager viewPager;
    List<Fragment> fragments = new ArrayList<Fragment>();
    ViewAdapter adapter;
    TabLayout tabLayout;
    /**
     * 图像搜索结果
     */
    private List<Commodity> commodityListGraph=new ArrayList<>();
    private List<PartUserInfo> partUserInfosGraph=new ArrayList<>();
    private List<PartUserInfo> commodityUserGraph=new ArrayList<>();
    private final MyHandler MyHandler =new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo);
        initView();
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_home_navigation);

        initFragment();
        /**
         * 登录云信
         */
        loginChatAccount();

    }

    /**
     *登录云信
     * @author 施立豪
     * @time 2023/4/29
     */
    private void loginChatAccount()
    {
        GraphService.getInstance(getApplicationContext());
    }
    /**
     * 初始化各个控件，找到对应的组件
     * @author 唐小莉
     * @time 2023/3/20 16:28
     */
    public void initView(){
        llTabOne =findViewById(R.id.ll_tab_one);
        llTabOne.setOnClickListener(this);
        llTabTwo =findViewById(R.id.ll_tab_two);
        llTabTwo.setOnClickListener(this);
        llTabFour =findViewById(R.id.ll_tab_four);
        llTabFour.setOnClickListener(this);
        llTabFive =findViewById(R.id.ll_tab_five);
        llTabFive.setOnClickListener(this);
        ivTabThree = (ImageView) findViewById(R.id.iv_tab_three);
        ivTabThree.setOnClickListener(this);
        llSearch = findViewById(R.id.linear_search_item);
        llSearch.setOnClickListener(this);
        imageSearch=findViewById(R.id.image_search1);
        imageSearch.setOnClickListener(this);
    }

    /**
     * 底部导航栏点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_tab_one:
                break;
            /**
             * 点击悬赏图标，进行跳转到悬赏界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_two:
                startActivity(new Intent(getApplicationContext(), XuanShangActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击中间加号按钮跳转选择界面+跳转动画
             * @Author: 李柏睿
             * @Time: 2023/3/22 16:38
             */
            case R.id.iv_tab_three:
                final RotateAnimation animation = new RotateAnimation(0.0f, 90.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration( 500 );
                ivTabThree.startAnimation( animation );
                startActivity(new Intent(getApplicationContext(),PublishSelectionActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击消息图标，进行跳转到消息界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_four:
                startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击个人中心图标，进行跳转到个人中心界面， overridePendingTransition(0, 0):去掉进场动画
             * @author 唐小莉
             * @time 2023/3/20 16:28
             */
            case R.id.ll_tab_five:
                startActivity(new Intent(getApplicationContext(), MyCenterActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击搜索跳转
             * @Author: 李柏睿
             * @Time: 2023/4/12 16:12
             */
            case R.id.linear_search_item:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(0,0);
                break;
            /**
             * 点击图片搜索
             */
            case R.id.image_search1:
                onChangeImage(view);



                break;

            default:
                break;
        }
    }
    /**
     * fragment初始化
     * @Author: 李柏睿
     * @Time: 2023/4/7 20:38
     */
    public void initFragment(){
        /**初始化数据*/
        fragments.add(new ConcernFragment("关注"));
        fragments.add(new SuggestFragment("推荐"));
        /**设置ViewPager适配器*/
        adapter = new ViewAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        /**设置初始fragment*/
        viewPager.setCurrentItem(1,false);


        /**
         * @param
         * @return: void
         * @Author: 李柏睿
         * @Time: 2023/4/7 22:24
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(ErHuoActivity.this, R.style.fragment_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(ErHuoActivity.this, R.style.fragment);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /**关联ViewPager*/
        tabLayout.setupWithViewPager(viewPager);
        /**设置固定的tab*/
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    /**
     * 消息发送接收，异步更新UI
     * @Author: 施立豪
     * @Time: 2023/5/10
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){

                /**
                 * 图像搜索
                 */
                case 2:
                    MyToast.toast(ErHuoActivity.this,"搜索完成",2);
                    /**
                     * 点击进行跳转并传值
                     */
                    Intent intent1=new Intent(getActivity(), SearchResultActivity.class);
                    Bundle bundle1=new Bundle();
                    bundle1.putSerializable("commodityList", (Serializable) commodityListGraph);
                    bundle1.putSerializable("partUserInfos", (Serializable) partUserInfosGraph);
                    bundle1.putSerializable("commodityUser", (Serializable) commodityUserGraph);
                    intent1.putExtras(bundle1);
                    intent1.putExtra("searchKey","");
                    startActivity(intent1);
                default:
                    break;
            }
        }
    }


    /**
     * 自定义沙盒文件处理
     */
    private static class MeSandboxFileEngine implements UriToFileTransformEngine {

        @Override
        public void onUriToFileAsyncTransform(Context context, String srcPath, String mineType, OnKeyValueResultCallbackListener call) {
            if (call != null) {
                call.onCallback(srcPath, SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType));
            }
        }
    }
    public Context getContext() {
        return this;
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private String getSandboxPath() {
        File externalFilesDir = getContext().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 自定义编辑
     */
    private static class MeOnMediaEditInterceptListener implements OnMediaEditInterceptListener {
        private final String outputCropPath;
        private final UCrop.Options options;

        public MeOnMediaEditInterceptListener(String outputCropPath, UCrop.Options options) {
            this.outputCropPath = outputCropPath;
            this.options = options;
        }
        @Override
        public void onStartMediaEdit(Fragment fragment, LocalMedia currentLocalMedia, int requestCode) {
            String currentEditPath = currentLocalMedia.getAvailablePath();
            Uri inputUri = PictureMimeType.isContent(currentEditPath)
                    ? Uri.parse(currentEditPath) : Uri.fromFile(new File(currentEditPath));
            Uri destinationUri = Uri.fromFile(
                    new File(outputCropPath, DateUtils.getCreateFileName("CROP_") + ".jpeg"));
            UCrop uCrop = UCrop.of(inputUri, destinationUri);
            options.setHideBottomControls(false);
            uCrop.withOptions(options);
            uCrop.setImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                    Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (call != null) {
                                call.onCall(resource);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (call != null) {
                                call.onCall(null);
                            }
                        }
                    });
                }
            });
            uCrop.startEdit(fragment.requireActivity(), fragment, requestCode);
        }}

    /**
     * 修改头像函数
     * @author  施立豪
     * @time 2023/4/9
     * @param view
     */
    public void onChangeImage(View view){
        Tracer tracer= GlobalTracer.get();
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setSelectorUIStyle(selectorStyle)
                .setCropEngine((CropEngine) null)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setVideoPlayerEngine(null)
                .setCompressEngine(new ImageFileCompressEngine())
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .setRecordAudioInterceptListener(null)
                .setSelectLimitTipsListener(null)
                .setEditMediaInterceptListener(new MeOnMediaEditInterceptListener(getSandboxPath(), buildOptions()))
                .setPermissionDescriptionListener(null)
                .setPreviewInterceptListener(null)
                .setPermissionDeniedListener(null)
                .setAddBitmapWatermarkListener(null)
                .setVideoThumbnailListener(null)
                .isAutoVideoPlay(false)
                .isLoopAutoVideoPlay(false)
                .isPageSyncAlbumCount(true)
                .setCustomLoadingListener(null)
                .setQueryFilterListener(new OnQueryFilterListener() {
                    @Override
                    public boolean onFilter(LocalMedia media) {
                        return false;
                    }
                })
                .setImageEngine(GlideEngine.createGlideEngine())
                //
                .setCropEngine((CropEngine) null)
                .isDisplayTimeAxis(false)
                .isOnlyObtainSandboxDir(false)
                .isPageStrategy(true)
                .isOriginalControl(false)
                .isDisplayCamera(true)
                .isOpenClickSound(false)
                .isFastSlidingSelect(false)

                .isWithSelectVideoImage(false)
                .isPreviewFullScreenMode(false)
                .isVideoPauseResumePlay(false)
                .isPreviewZoomEffect(false)
                .isPreviewImage(true)
                .isPreviewVideo(false)
                .isPreviewAudio(false)
                .setGridItemSelectAnimListener(null)
                .setSelectAnimListener( null)
                .isMaxSelectEnabledMask(true)
                .isDirectReturnSingle(false)
                //选择图片数量
                .setMaxSelectNum(1)
                .setMaxVideoSelectNum(0)
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)
                .isGif(false)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        LocalMedia media = result.get(0);
                        Log.i(TAG, "文件名: " + media.getFileName());
                        Log.i(TAG, "是否压缩:" + media.isCompressed());
                        Log.i(TAG, "压缩:" + media.getCompressPath());
                        Log.i(TAG, "初始路径:" + media.getPath());
                        Log.i(TAG, "绝对路径:" + media.getRealPath());
                        Log.i(TAG, "是否裁剪:" + media.isCut());
                        Log.i(TAG, "裁剪路径:" + media.getCutPath());
                        Log.i(TAG, "是否开启原图:" + media.isOriginal());
                        Log.i(TAG, "原图路径:" + media.getOriginalPath());
                        Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
                        Log.i(TAG, "水印路径:" + media.getWatermarkPath());
                        Log.i(TAG, "视频缩略图:" + media.getVideoThumbnailPath());
                        Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
                        if (media.getWidth() != media.getHeight())
                        {Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());}
                        Log.i(TAG, "文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.getSize()));
                        Log.i(TAG, "文件时长: " + media.getDuration());
                        /**
                         * 文件命名，使用裁剪后的文件名，是由裁剪时间命名的，重名概率较小，为确保不会重名，后续可添加唯一标识id。避免同名覆盖
                         * @aythor 施立豪
                         * @time 2023/4/9
                         */
                        String filePath = media.getSandboxPath();

                        /**
                         * 子线程，将待进行oss的图片url插入用户信息数据库
                         * 先插入数据库，再进行oss
                         * @author 施立豪
                         * @time 2023/4/9
                         */
                        MyToast.toast(ErHuoActivity.this,"正在搜索匹配的商品",2);
                        new Thread(()->{
                            /**
                             * 接收插入mysql结果变量
                             */
                            List<Integer> commodityIdList= GraphOperate.productSearch(filePath);
                            /**
                             * 建立连接对象
                             */
                            LettuceBaseCase lettuce=new LettuceBaseCase();
                            /**
                             * 获取连接
                             */
                            RedisCommands<String, String> con=lettuce.getSyncConnection();
                            commodityUserGraph.clear();commodityListGraph.clear();
                            for(Integer i:commodityIdList)
                            {
                                Commodity comm= CommodityOperate.getCommodityFromRedis(con,i,ErHuoActivity.this);
                                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,comm.getUserId(),ErHuoActivity.this);
                                commodityUserGraph.add(part);
                                commodityListGraph.add(comm);
                            }
                            Message msg=Message.obtain();
                            msg.arg1=2;
                            MyHandler.sendMessage(msg);
                        }).start();

//                       /**
//                         * 查询全局变量头像url，更新头像
//                         */
//                        String imageUrl=UserInfo.getUrl();
//                        if(!imageUrl.equals(""))
//                        {
//                            user_image.setImageURI(Uri.fromFile(new File(imageUrl)));
//                            SharedPreferences.Editor editor = imagePath.edit();
//                            editor.putString("imagepath",imageUrl);
//                            editor.apply();
//                        }
//
                    }

                    @Override
                    public void onCancel() {

                    }
                });

    }



    /**
     * 自定义压缩
     */
    private static class ImageFileCompressEngine implements CompressFileEngine {

        @Override
        public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
            Luban.with(context).load(source).ignoreBy(100).setRenameListener(new OnRenameListener() {
                @Override
                public String rename(String filePath) {
                    int indexOf = filePath.lastIndexOf(".");
                    String postfix = indexOf != -1 ? filePath.substring(indexOf) : ".jpg";
                    return DateUtils.getCreateFileName("CMP_") + postfix;
                }
            }).filter(new CompressionPredicate() {
                @Override
                public boolean apply(String path) {
                    if (PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path)) {
                        return true;
                    }
                    return !PictureMimeType.isUrlHasGif(path);
                }
            }).setCompressListener(new OnNewCompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(String source, File compressFile) {
                    if (call != null) {
                        call.onCallback(source, compressFile.getAbsolutePath());
                    }
                }

                @Override
                public void onError(String source, Throwable e) {
                    if (call != null) {
                        call.onCallback(source, null);
                    }
                }
            }).launch();
        }
    }



    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    @SuppressLint("ResourceAsColor")
    private UCrop.Options buildOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setShowCropFrame(false);
        options.setShowCropGrid(false);
        options.setCircleDimmedLayer(true);
        options.withAspectRatio(1, 1);
        options.setCropOutputPathDir("");
        options.isCropDragSmoothToCenter(false);
        options.isForbidCropGifWebp(false);
        options.isForbidSkipMultipleCrop(true);
        options.setMaxScaleMultiplier(100);
        if (selectorStyle != null && selectorStyle.getSelectMainStyle().getStatusBarColor() != 0) {
            SelectMainStyle mainStyle = selectorStyle.getSelectMainStyle();
            boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
            int statusBarColor = mainStyle.getStatusBarColor();
            options.isDarkStatusBarBlack(isDarkStatusBarBlack);
            if (StyleUtils.checkStyleValidity(statusBarColor)) {
                options.setStatusBarColor(statusBarColor);
                options.setToolbarColor(statusBarColor);
            } else {
                options.setStatusBarColor( R.color.gray);
                options.setToolbarColor( R.color.gray);
            }
            TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
            if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleTextColor())) {
                options.setToolbarWidgetColor(titleBarStyle.getTitleTextColor());
            } else {
                options.setToolbarWidgetColor(R.color.white);
            }
        } else {
            options.setStatusBarColor( R.color.gray);
            options.setToolbarColor( R.color.gray);
            options.setToolbarWidgetColor(R.color.white);
        }
        return options;
    }
    /**
     * 自定义裁剪
     */
    private class ImageFileCropEngine implements CropFileEngine {

        @Override
        public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
            UCrop.Options options = buildOptions();
            UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
            uCrop.withOptions(options);
            uCrop.setImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                    Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (call != null) {
                                call.onCall(resource);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (call != null) {
                                call.onCall(null);
                            }
                        }
                    });
                }
            });
            uCrop.start(fragment.requireActivity(), fragment, requestCode);
        }
    }
}