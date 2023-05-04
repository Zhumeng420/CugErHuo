package com.example.cugerhuo.activity.post;

import static com.example.cugerhuo.activity.adapter.GridImageAdapter.TAG;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Reward;
import com.example.cugerhuo.access.reward.RewardOperate;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.adapter.GridImageAdapter;
import com.example.cugerhuo.activity.listener.OnItemLongClickListener;
import com.example.cugerhuo.tools.GetFileNameUtil;
import com.example.cugerhuo.tools.MyToast;
import com.example.cugerhuo.tools.OssOperate;
import com.example.cugerhuo.tools.TracingHelper;
import com.example.cugerhuo.tools.photoselect.GlideEngine;
import com.example.cugerhuo.tools.photoselect.ImageLoaderUtils;
import com.example.cugerhuo.tools.select.CustomPreviewFragment;
import com.example.cugerhuo.tools.select.FullyGridLayoutManager;
import com.luck.lib.camerax.CameraImageEngine;
import com.luck.lib.camerax.SimpleCameraX;
import com.luck.picture.lib.PictureSelectorPreviewFragment;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.IBridgeViewLifecycle;
import com.luck.picture.lib.basic.PictureSelectionCameraModel;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelectionSystemModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.InjectResourceSource;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.config.SelectLimitType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.engine.CompressEngine;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.engine.CropEngine;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.engine.ExtendLoaderEngine;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.engine.UriToFileTransformEngine;
import com.luck.picture.lib.engine.VideoPlayerEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnCallbackListener;
import com.luck.picture.lib.interfaces.OnCameraInterceptListener;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnGridItemSelectAnimListener;
import com.luck.picture.lib.interfaces.OnInjectActivityPreviewListener;
import com.luck.picture.lib.interfaces.OnInjectLayoutResourceListener;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnPermissionDeniedListener;
import com.luck.picture.lib.interfaces.OnQueryAlbumListener;
import com.luck.picture.lib.interfaces.OnQueryAllAlbumListener;
import com.luck.picture.lib.interfaces.OnQueryDataResultListener;
import com.luck.picture.lib.interfaces.OnQueryFilterListener;
import com.luck.picture.lib.interfaces.OnRecordAudioInterceptListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener;
import com.luck.picture.lib.interfaces.OnVideoThumbnailEventListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.loader.SandboxFileLoader;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.permissions.PermissionConfig;
import com.luck.picture.lib.permissions.PermissionResultCallback;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.DateUtils;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.PictureFileUtils;
import com.luck.picture.lib.utils.SandboxTransformUtils;
import com.luck.picture.lib.utils.SdkVersionUtils;
import com.luck.picture.lib.utils.StyleUtils;
import com.luck.picture.lib.utils.ToastUtils;
import com.luck.picture.lib.widget.MediumBoldTextView;
import com.tencent.cos.xml.CIService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.ci.audit.GetTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.PostTextAuditRequest;
import com.tencent.cos.xml.model.ci.audit.TextAuditResult;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudSelfSigner;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.QCloudHttpRequest;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnNewCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 * @author：李柏睿
 * @data：2023/4/21
 * @描述: 悬赏页面
 */
public class PostBuyActivity extends AppCompatActivity implements  View.OnClickListener{
    /**取消*/
    private TextView cancelXuanShang;
    /**发布*/
    private Button postBuy;
    /**
     * 悬赏
     */
    private Reward reward;
    /**悬赏输入框*/
    private EditText inputNeed;
    /**地理位置*/
    private TextView locationText;
    /**
     * 线程池
     */
    private ThreadPoolExecutor executor = null;
    /**定位*/
    private String location;
    /**定位服务*/
    /**声明AMapLocationClient类对象*/
    AMapLocationClient mLocationClient = null;
    private final PostBuyActivity.MyHandler MyHandler =new PostBuyActivity.MyHandler(Looper.getMainLooper());

    /**添加图片*/
    private ActivityResultLauncher<Intent> launcherResult;
    private GridImageAdapter mAdapter;
    private final List<LocalMedia> mData = new ArrayList<>();
    private ImageEngine imageEngine;
    private VideoPlayerEngine videoPlayerEngine;
    private PictureSelectorStyle selectorStyle;
    private int language = LanguageConfig.UNKNOWN_LANGUAGE;
    private int chooseMode = SelectMimeType.ofAll();
    private final static String TAG_EXPLAIN_VIEW = "TAG_EXPLAIN_VIEW";
    private final static int ACTIVITY_RESULT = 1;
    private final static int CALLBACK_RESULT = 2;
    private final static int LAUNCHER_RESULT = 3;
    private int resultMode = LAUNCHER_RESULT;
    private int animationMode = AnimationType.DEFAULT_ANIMATION;
    private int aspect_ratio_x = -1, aspect_ratio_y = -1;
    /**
     * 存放图片视频路径
     */
    private List paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100);
        setContentView(R.layout.activity_post_buy);
        paths=new ArrayList<String>();
        /**
         * 创建线程池
         */
        executor=new ThreadPoolExecutor(
                //核心线程数
                0,
                //最大线程数
                2,
                //等待时间
                1L,
                //时间单位
                TimeUnit.SECONDS,
                //最大任务数
                new ArrayBlockingQueue<>(10),
                //饱和策略
                new ThreadPoolExecutor.CallerRunsPolicy());
        initView();
        try {
            getLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化，找到各控件
     * @Author: 李柏睿
     * @Time: 2023/5/3 15:27
     */
    public void initView(){
        cancelXuanShang = findViewById(R.id.cancel_xuanshang);
        cancelXuanShang.setOnClickListener(this);
        postBuy = findViewById(R.id.publishXuanShang);
        postBuy.setOnClickListener(this);
        inputNeed = findViewById(R.id.post_reason);
        locationText = findViewById(R.id.location_xuanshang);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_pic);
        // 注册需要写在onCreate或Fragment onAttach里，否则会报java.lang.IllegalStateException异常
        launcherResult = createActivityResultLauncher();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        RecyclerView.ItemAnimator itemAnimator = mRecyclerView.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                DensityUtil.dip2px(this, 8), false));
        mAdapter = new GridImageAdapter(getContext(), mData);
        mAdapter.setSelectMax(8);
        mRecyclerView.setAdapter(mAdapter);

        String systemHigh = " (仅支持部分api)";
        String systemTips = "使用系统图库" + systemHigh;
        int startIndex = systemTips.indexOf(systemHigh);
        int endOf = startIndex + systemHigh.length();
        SpannableStringBuilder builder = new SpannableStringBuilder(systemTips);
        builder.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(getContext(), 12)), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(0xFFCC0000), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        String cameraHigh = " (默认fragment)";
        String cameraTips = "使用Activity承载Camera相机" + cameraHigh;
        int startIndex2 = cameraTips.indexOf(cameraHigh);
        int endOf2 = startIndex2 + cameraHigh.length();
        SpannableStringBuilder builder2 = new SpannableStringBuilder(cameraTips);
        builder2.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(getContext(), 12)), startIndex2, endOf2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        String systemAlbumHigh = " (默认fragment)";
        String systemAlbumTips = "使用Activity承载系统相册" + systemAlbumHigh;
        int startIndex3 = systemAlbumTips.indexOf(systemAlbumHigh);
        int endOf3 = startIndex3 + systemAlbumHigh.length();
        SpannableStringBuilder builder3 = new SpannableStringBuilder(systemAlbumTips);
        builder3.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(getContext(), 12)), startIndex3, endOf3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        imageEngine = GlideEngine.createGlideEngine();

        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 预览图片、视频、音频
                PictureSelector.create(PostBuyActivity.this)
                        .openPreview()
                        .setImageEngine(imageEngine)
                        .setVideoPlayerEngine(videoPlayerEngine)
                        .setSelectorUIStyle(selectorStyle)
                        .setLanguage(language)
                        .isAutoVideoPlay(false)
                        .isLoopAutoVideoPlay(false)
                        .isPreviewFullScreenMode(false)
                        .isVideoPauseResumePlay(false)
                        .setCustomLoadingListener(null)
                        .isPreviewZoomEffect(chooseMode != SelectMimeType.ofAudio() && false, mRecyclerView)
                        .setAttachViewLifecycle(new IBridgeViewLifecycle() {
                            @Override
                            public void onViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {

                            }

                            @Override
                            public void onDestroy(Fragment fragment) {

                            }
                        })
                        .setInjectLayoutResourceListener(new OnInjectLayoutResourceListener() {
                            @Override
                            public int getLayoutResourceId(Context context, int resourceSource) {
                                return resourceSource == InjectResourceSource.PREVIEW_LAYOUT_RESOURCE
                                        ? R.layout.ps_custom_fragment_preview
                                        : InjectResourceSource.DEFAULT_LAYOUT_RESOURCE;
                            }
                        })
                        .setExternalPreviewEventListener(new PostBuyActivity.MyExternalPreviewEventListener())
                        .setInjectActivityPreviewFragment(new OnInjectActivityPreviewListener() {
                            @Override
                            public PictureSelectorPreviewFragment onInjectPreviewFragment() {
                                return false ? CustomPreviewFragment.newInstance() : null;
                            }
                        })
                        .startActivityPreview(position, true, mAdapter.getData());
            }

            @Override
            public void openPicture() {
                boolean mode = true;
                if (mode) {
                    // 进入系统相册
                    if (false) {
                        PictureSelectionSystemModel systemGalleryMode = PictureSelector.create(getContext())
                                .openSystemGallery(chooseMode)
                                .setSelectionMode(true ? SelectModeConfig.MULTIPLE : SelectModeConfig.SINGLE)
                                .setCompressEngine(getCompressFileEngine())
                                .setCropEngine(getCropFileEngine())
                                .setSkipCropMimeType(getNotSupportCrop())
                                .setSelectLimitTipsListener(new PostBuyActivity.MeOnSelectLimitTipsListener())
                                .setAddBitmapWatermarkListener(null)
                                .setVideoThumbnailListener(null)
                                .setCustomLoadingListener(null)
                                .isOriginalControl(false)
                                .setPermissionDescriptionListener(null)
                                .setSandboxFileEngine(new PostBuyActivity.MeSandboxFileEngine());
                        forSystemResult(systemGalleryMode);
                    } else {
                        // 进入相册
                        PictureSelectionModel selectionModel = PictureSelector.create(getContext())
                                .openGallery(chooseMode)
                                .setSelectorUIStyle(selectorStyle)
                                .setImageEngine(imageEngine)
                                .setVideoPlayerEngine(videoPlayerEngine)
                                .setCompressEngine(getCompressFileEngine())
                                .setSandboxFileEngine(new PostBuyActivity.MeSandboxFileEngine())
                                .setCameraInterceptListener(getCustomCameraEvent())
                                .setRecordAudioInterceptListener(new PostBuyActivity.MeOnRecordAudioInterceptListener())
                                .setSelectLimitTipsListener(new PostBuyActivity.MeOnSelectLimitTipsListener())
                                .setEditMediaInterceptListener(null)
                                .setPermissionDescriptionListener(null)
                                .setPreviewInterceptListener(null)
                                .setPermissionDeniedListener(getPermissionDeniedListener())
                                .setAddBitmapWatermarkListener(null)
                                .setVideoThumbnailListener(null)
                                .isAutoVideoPlay(true)
                                .isLoopAutoVideoPlay(true)
                                .isPageSyncAlbumCount(true)
                                .setCustomLoadingListener(null)
                                .setQueryFilterListener(new OnQueryFilterListener() {
                                    @Override
                                    public boolean onFilter(LocalMedia media) {
                                        return false;
                                    }
                                })
                                //.setExtendLoaderEngine(getExtendLoaderEngine())
                                .setInjectLayoutResourceListener(getInjectLayoutResource())
                                .setSelectionMode(true ? SelectModeConfig.MULTIPLE : SelectModeConfig.SINGLE)
                                .setLanguage(language)
                                .setQuerySortOrder(false ? MediaStore.MediaColumns.DATE_MODIFIED + " ASC" : "")
                                .setOutputCameraDir(chooseMode == SelectMimeType.ofAudio()
                                        ? null : "")
                                .setOutputAudioDir(chooseMode == SelectMimeType.ofAudio()
                                        ? null : "")
                                .setQuerySandboxDir(chooseMode == SelectMimeType.ofAudio()
                                        ? null : "")
                                .isDisplayTimeAxis(false)
                                .isOnlyObtainSandboxDir(false)
                                .isPageStrategy(false)
                                .isOriginalControl(false)
                                .isDisplayCamera(true)
                                .isOpenClickSound(false)
                                .setSkipCropMimeType(getNotSupportCrop())
                                .isFastSlidingSelect(true)
                                .isWithSelectVideoImage(true)
                                .isPreviewFullScreenMode(true)
                                .isVideoPauseResumePlay(true)
                                .isPreviewZoomEffect(true)
                                .isPreviewImage(true)
                                .isPreviewVideo(true)
                                .isPreviewAudio(true)
                                .setGridItemSelectAnimListener(true ? new OnGridItemSelectAnimListener() {

                                    @Override
                                    public void onSelectItemAnim(View view, boolean isSelected) {
                                        AnimatorSet set = new AnimatorSet();
                                        set.playTogether(
                                                ObjectAnimator.ofFloat(view, "scaleX", isSelected ? 1F : 1.12F, isSelected ? 1.12f : 1.0F),
                                                ObjectAnimator.ofFloat(view, "scaleY", isSelected ? 1F : 1.12F, isSelected ? 1.12f : 1.0F)
                                        );
                                        set.setDuration(350);
                                        set.start();
                                    }
                                } : null)

                                //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
                                .isMaxSelectEnabledMask(true)
                                .isDirectReturnSingle(false)
                                .setMaxSelectNum(8)
                                .setMaxVideoSelectNum(1)
                                .setRecyclerAnimationMode(animationMode)
                                .isGif(false)
                                .setSelectedData(mAdapter.getData());
                        forSelectResult(selectionModel);
                    }
                } else {
                    // 单独拍照
                    PictureSelectionCameraModel cameraModel = PictureSelector.create(PostBuyActivity.this)
                            .openCamera(chooseMode)
                            .setCameraInterceptListener(getCustomCameraEvent())
                            .setRecordAudioInterceptListener(new PostBuyActivity.MeOnRecordAudioInterceptListener())
                            .setCropEngine(getCropFileEngine())
                            .setCompressEngine(getCompressFileEngine())
                            .setSelectLimitTipsListener(new PostBuyActivity.MeOnSelectLimitTipsListener())
                            .setAddBitmapWatermarkListener(null)
                            .setVideoThumbnailListener(getVideoThumbnailEventListener())
                            .setCustomLoadingListener(null)
                            .setLanguage(language)
                            .setSandboxFileEngine(new PostBuyActivity.MeSandboxFileEngine())
                            .isOriginalControl(false)
                            .setPermissionDescriptionListener(null)
                            .setOutputAudioDir(null)
                            .setSelectedData(mAdapter.getData());
                    forOnlyCameraResult(cameraModel);
                }
            }
        });

        mAdapter.setItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder holder, int position, View v) {
                int itemViewType = holder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
//                    mItemTouchHelper.startDrag(holder);
                }
            }
        });

    }

    /**
     * 点击事件函数
     * @Author: 李柏睿
     * @Time: 2023/4/22 20:12
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            /**
             * 取消
             * @Author: 李柏睿
             * @Time: 2023/5/3
             */
            case R.id.cancel_xuanshang:
                finish();
                break;
            /**
             * 发布
             * @Author: 李柏睿
             * @Time: 2023/5/3
             */
            case R.id.publishXuanShang:
                Reward reward=new Reward();
                if(inputNeed.getText()==null||inputNeed.getText().length()==0)
                {
                    MyToast.toast(PostBuyActivity.this,"请填写物品描述",0);
                    break;
                }
                else
                {

                }
                StringBuilder tempU=new StringBuilder();
                for( int i=0;i<paths.size();++i)
                {
                    tempU.append("specific_"+ GetFileNameUtil.getFileName((String) paths.get(i)));
                    tempU.append(";");

                }
                if(tempU!=null){
                    reward.setUrl(tempU.toString());
                }
                reward.setState(1);
                Date date = new Date(System.currentTimeMillis());
                reward.setDescription(inputNeed.getText().toString());
                reward.setLocation(location);
                reward.setTime(date);
                reward.setUserid(UserInfo.getid());
                MyToast.toast(PostBuyActivity.this,"正在审核",2);
                this.reward=reward;
                AuditTask auditTask=new PostBuyActivity.AuditTask(inputNeed.getText().toString());
                executor.execute(auditTask);
                break;
            default:
                break;
        }
    }

    /**
     * 字符串转字符数组
     */
    private String[] arrayOf(String accessFineLocation, String accessBackgroundLocation) {
        return new String[]{accessBackgroundLocation,accessFineLocation};
    }
    /**
     * 执行文本审核线程
     * @author 施立豪
     */
    public  class AuditTask implements Runnable {

        //private PublishTask publish;
        /**
         * 待审核文本
         */
        private String text;

        /**
         * 构造函数传参
         * @param text  审核文本
         */
        public AuditTask(String text) {
            this.text=text;
        }
        @Override
        public void run() {
            String secretId = "AKIDZzNl3Dv33zjW9iF4vkoihXGn7FI7g4GH"; //用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            String secretKey = "PgmiWAduzwqWH3FLHQSIsCKK6eyH9b0T"; //用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
// keyDuration 为请求中的密钥有效期，单位为秒
            QCloudCredentialProvider myCredentialProvider =
                    new ShortTimeCredentialProvider(secretId, secretKey, 300);
            QCloudSelfSigner myQCloudSelfSigner = new QCloudSelfSigner() {
                /**
                 * 对请求进行签名
                 *
                 * @param request 需要签名的请求
                 * @throws QCloudClientException 客户端异常
                 */
                @Override
                public void sign(QCloudHttpRequest request) throws QCloudClientException {
                    // 1. 把 request 的请求参数传给服务端计算签名
                    String auth = "get auth from server";
                    // 2. 给请求添加签名
                    request.addHeader(HttpConstants.Header.AUTHORIZATION, auth);
                }
            };
            // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
            String region = "ap-beijing";
// 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
            CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                    .setRegion(region)
                    .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                    .builder();
            CIService ciService=new CIService(PostBuyActivity.this,serviceConfig,myCredentialProvider);
            // 存储桶名称，格式为 BucketName-APPID
            String bucket = "cug-erhuo-1314485188";
            // 对象键，是对象在 COS 上的完整路径，如果带目录的话，格式为 "dir1/object1"
            String cosPath = "exampleobject.txt";
            //文本的链接地址,Object 和 Url 只能选择其中一种
            String url = "https://myqcloud.com/%205text.txt";
            //当传入的内容为纯文本信息，需要先经过 base64 编码，文本编码前的原文长度不能超过10000个 utf8 编码字符。若超出长度限制，接口将会报错。
            String content = Base64.encodeToString((text).getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP);
            PostTextAuditRequest request = new PostTextAuditRequest(bucket);
            request.setObject(cosPath);
            request.setUrl(url);
            request.setContent(content);
            //设置原始内容，长度限制为512字节，该字段会在响应中原样返回
            request.setDataId("DataId");
            //回调地址，以http://或者https://开头的地址。
            request.setCallback("https://github.com");
            //回调内容的结构，有效值：Simple（回调内容包含基本信息）、Detail（回调内容包含详细信息）。默认为 Simple。
            request.setCallbackVersion("Detail");
            //审核的场景类型，有效值：Porn（涉黄）、Ads（广告），可以传入多种类型，不同类型以逗号分隔，例如：Porn,Ads。
            request.setDetectType("Porn,Ads");
            // CIService 是 CosXmlService 的子类，初始化方法和 CosXmlService 一致
            ciService.postTextAuditAsync(request, new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                    // result 提交文本审核任务的结果
                    // 详细字段请查看api文档或者SDK源码
                    TextAuditResult result = (TextAuditResult) cosResult;
                    String jobId ;
                    jobId= result.textAuditJobResponse.jobsDetail.jobId;
                    System.out.println(jobId);
                    GetTextAuditRequest request1 = new GetTextAuditRequest(bucket, jobId);
                    // CIService 是 CosXmlService 的子类，初始化方法和 CosXmlService 一致
                    ciService.getTextAuditAsync(request1, new CosXmlResultListener() {
                        @Override
                        public void onSuccess(CosXmlRequest request, CosXmlResult cosResult) {
                            // result 查询文本审核任务的结果
                            // 详细字段请查看 API 文档或者 SDK 源码
                            TextAuditResult result = (TextAuditResult) cosResult;
                            Message msg =new Message();
                            msg.arg1=6;
                            /**
                             * 判断审核结果是否正确
                             */
                            switch (result.textAuditJobResponse.jobsDetail.label)
                            {
                                case "Normal":
                                    msg.arg2=0;
                                    break;
                                case "Porn":
                                    msg.arg2=1;
                                    break;
                                case "Ads":
                                    msg.arg2=2;
                                    break;
                                default:
                                    msg.arg2=3;
                                    break;
                            };
                            MyHandler.sendMessage(msg);

                        }
                        @Override
                        public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                            Message msg =new Message();
                            msg.arg1=7;
                            MyHandler.sendMessage(msg);
                            if (clientException != null) {
                                clientException.printStackTrace();
                            } else {
                                serviceException.printStackTrace();
                            }
                        }
                    });
                }
                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    Message msg =new Message();
                    msg.arg1=7;
                    MyHandler.sendMessage(msg);
                    if (clientException != null) {
                        clientException.printStackTrace();
                    } else {
                        serviceException.printStackTrace();
                    }
                }
            });
        }

    }
    /**
     * 判断权限是否存在
     */
    public boolean lacksPermission(String[] permissions) {
        for (String permission : permissions) {
            //判断是否缺少权限，true=缺少权限
            if(ContextCompat.checkSelfPermission(PostBuyActivity.this, permission) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    /**
     * 请求权限回调结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults[1]!=0&&grantResults[0]!=0) {
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(PostBuyActivity.this,"未拥有定位相应权限",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    //拥有权限执行操作
                } else {
                    try {
                        getLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     *获取定位
     * @author 施立豪
     * @time 2023/5/3
     */
    void getLocation() throws Exception {
        //如果具有权限，则执行获取定位接口
        if(!lacksPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})){
            //声明定位回调监听器
            AMapLocationListener mLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0)
                        {
                            if (location == null || "".equals(location)) {
                                location = amapLocation.getAoiName();//获取当前定位点的AOI信息
                                Message msg = Message.obtain();
                                msg.arg1 = 1;
                                MyHandler.sendMessage(msg);
                                mLocationClient.stopLocation();
                            }
                            else {
                                String now = amapLocation.getAoiName();
                                if(!location.equals(now)){
                                    Message msg = Message.obtain();
                                    msg.arg1 = 1;
                                    MyHandler.sendMessage(msg);
                                }
                            }
                        }
                        else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError","location Error, ErrCode:"
                                    + amapLocation.getErrorCode() + ", errInfo:"
                                    + amapLocation.getErrorInfo());
                        }
                    }
                }
            };
            AMapLocationClient.setApiKey("9f769df4a1a71a366247dc9da243750b");
            AMapLocationClient.updatePrivacyAgree(PostBuyActivity.this,true);
            AMapLocationClient.updatePrivacyShow(PostBuyActivity.this,true,true);
            mLocationClient = new AMapLocationClient(PostBuyActivity.this);
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            AMapLocationClientOption mLocationOption = null;
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            AMapLocationClientOption option = new AMapLocationClientOption();
            /**
             * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
             */
            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            mLocationOption.setOnceLocation(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }}

    /**
     * 消息发送接收，异步更新UI
     * @author 施立豪
     * @time 2023/4/19
     */
    private class MyHandler extends Handler {
        public MyHandler(Looper mainLooper) {

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                /**
                 * 定位信息更新
                 */
                case 1:
                    if(location!=null)
                    {
                        locationText.setText(location);
                        mLocationClient.stopLocation();
                    }
                    break;
                case 4:
                    MyToast.toast(PostBuyActivity.this,"发布成功",3);
                    break;
                case 7:
                    MyToast.toast(PostBuyActivity.this,"抱歉,文本审核功能遇到了问题！",0);
                    break;
                case 6:
                    //TODO 处理发布
                    switch (msg.arg2)
                    {
                        case 0:
                            MyToast.toast(PostBuyActivity.this,"正在上传",2);
                            PublishTask publishTask=new PublishTask(reward,UserInfo.getid());
                            executor.execute(publishTask);
                            break;
                        case 1:
                            MyToast.toast(PostBuyActivity.this,"您的输入存在色情内容，禁止发布",0);
                            break;
                        case 2:
                            MyToast.toast(PostBuyActivity.this,"您的输入中检测到广告，禁止发布",0);
                            break;
                        default:
                            MyToast.toast(PostBuyActivity.this,"抱歉,未知错误",1);
                            break;
                    }

                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 执行发布任务线程
     */
    public class PublishTask implements Runnable
    {
        private int id;
        private Reward reward;

        /**
         * 发布构造，传参
         * @param reward  商品信息
         * @param userId    用户id
         */
        public PublishTask(Reward reward,int userId)
        {
            this.reward=reward;
            id=userId;
        }
        @Override
        public void run() {
            {
                Tracer tracer = GlobalTracer.get();
                int result =0;
                // 创建spann
                Span span = tracer.buildSpan("发布流程").withTag("onBtnClickedListener：", "子踪").start();
                try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                    // 插入mysql并获取id
                    result= RewardOperate.insertReward(reward,PostBuyActivity.this);
                    if(result>0)
                    {
                        Log.i(TAG,"插入商品至mysql成功");
                        //插入图数据库
                        boolean result1=false;

                        {

                            /**
                             * 上传商品信息
                             */
                            List<String> imageSearch=new ArrayList<>();

                            for(int i=0;i< paths.size();++i)
                            {             imageSearch.add((String) paths.get(i));

                                {
                                    String fileName = "specific_"+GetFileNameUtil.getFileName((String) paths.get(i));
                                    boolean isUped=false;
                                    Span span1 = tracer.buildSpan("上传悬赏到oss流程").withTag("onChangeImage函数：", "子追踪").start();
                                    try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
                                        // 业务逻辑写这里
                                        isUped= OssOperate.up(fileName, Uri.fromFile(new File((String) paths.get(i))));
                                        if(isUped) {
                                            Log.i(TAG, "上传商品oss上传成功");
                                        }
                                        else{
                                            Log.e(TAG,"上传商品oss上传失败");
                                        }
                                        //UserInfo.setUrl(fileName);
                                    } catch (Exception e) {
                                        TracingHelper.onError(e, span);
                                        throw e;
                                    } finally {
                                        span.finish();
                                    }
                                }
                            }
//                            Span span1 = tracer.buildSpan("上传到oss流程").withTag("onChangeImage函数：", "子追踪").start();
//                            try (Scope ignored1 = tracer.scopeManager().activate(span,true)) {
//                                // 业务逻辑写这里
//                                Boolean a= GraphOperate.productAdd1(imageSearch,result);
//                                if(a) {
//                                    Log.i(TAG, "上传成功");
//                                }
//                                else{
//                                    Log.e(TAG,"oss上传失败");
//                                }
//                            } catch (Exception e) {
//                                TracingHelper.onError(e, span);
//                                throw e;
//                            } finally {
//                                span.finish();
//                            }
                            /**
                             * 通知UI发生提醒
                             */
                            Message msg = Message.obtain();
                            msg.arg1 = 4;
                            MyHandler.sendMessage(msg);

                        }

                    }
                    else
                    {
                        Log.e(TAG,"插入商品至mysql失败");
                    }
                    //插入图数据库

                } catch (Exception e) {
                    TracingHelper.onError(e, span);

                } finally {
                    span.finish();
                }
                if(result>0)
                {
                    Log.i(TAG,"发布成功");
                }else
                {
                    Log.e(TAG,"发布失败");
                }

            }

        }
    }
    /**
     * 图片选择
     */
    private String[] getNotSupportCrop() {
        if (false) {
            return new String[]{PictureMimeType.ofGIF(), PictureMimeType.ofWEBP()};
        }
        return null;
    }

    /**
     * 处理选择结果
     *
     * @param result
     */
    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(getContext(), media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(getContext(), media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            Log.i("TAG", "文件名: " + media.getFileName());
            Log.i("TAG", "是否压缩:" + media.isCompressed());
            Log.i("TAG", "压缩:" + media.getCompressPath());
            Log.i("TAG", "初始路径:" + media.getPath());
            Log.i("TAG", "绝对路径:" + media.getRealPath());
            Log.i("TAG", "是否裁剪:" + media.isCut());
            Log.i("TAG", "裁剪路径:" + media.getCutPath());
            Log.i("TAG", "是否开启原图:" + media.isOriginal());
            Log.i("TAG", "原图路径:" + media.getOriginalPath());
            Log.i("TAG", "沙盒路径:" + media.getSandboxPath());
            Log.i("TAG", "水印路径:" + media.getWatermarkPath());
            Log.i("TAG", "视频缩略图:" + media.getVideoThumbnailPath());
            Log.i("TAG", "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i("TAG", "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i("TAG", "文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.getSize()));
            Log.i("TAG", "文件时长: " + media.getDuration());
            setPaths(result);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isMaxSize = result.size() == mAdapter.getSelectMax();
                int oldSize = mAdapter.getData().size();
                mAdapter.notifyItemRangeRemoved(0, isMaxSize ? oldSize + 1 : oldSize);
                mAdapter.getData().clear();

                mAdapter.getData().addAll(result);
                mAdapter.notifyItemRangeInserted(0, result.size());
            }
        });
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
     * 选择结果
     */
    private class MeOnResultCallbackListener implements OnResultCallbackListener<LocalMedia> {
        @Override
        public void onResult(ArrayList<LocalMedia> result) {
            analyticalSelectResults(result);
        }

        @Override
        public void onCancel() {
            Log.i("TAG", "PictureSelector Cancel");
        }
    }

    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    private ActivityResultLauncher<Intent> createActivityResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            ArrayList<LocalMedia> selectList = PictureSelector.obtainSelectorList(result.getData());
                            analyticalSelectResults(selectList);
                        } else if (resultCode == RESULT_CANCELED) {
                            Log.i("TAG", "onActivityResult PictureSelector Cancel");
                        }
                    }
                });
    }

    /**
     * 外部预览监听事件
     */
    private class MyExternalPreviewEventListener implements OnExternalPreviewEventListener {

        @Override
        public void onPreviewDelete(int position) {
            mAdapter.remove(position);
            mAdapter.notifyItemRemoved(position);
        }

        @Override
        public boolean onLongPressDownload(Context context, LocalMedia media) {
            return false;
        }
    }

    /**
     * get上传文件
     * @author 施立豪
     * @time 2023/4/19
     */
    void setPaths(List<LocalMedia> result)
    {
        paths.clear();
        for(LocalMedia i :result)
        {
            paths.add(i.getSandboxPath());
            System.out.println(i.getSandboxPath());}

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
     * 自定义压缩
     */
    @Deprecated
    private static class ImageCompressEngine implements CompressEngine {

        @Override
        public void onStartCompress(Context context, ArrayList<LocalMedia> list,
                                    OnCallbackListener<ArrayList<LocalMedia>> listener) {
            // 自定义压缩
            List<Uri> compress = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                LocalMedia media = list.get(i);
                String availablePath = media.getAvailablePath();
                Uri uri = PictureMimeType.isContent(availablePath) || PictureMimeType.isHasHttp(availablePath)
                        ? Uri.parse(availablePath)
                        : Uri.fromFile(new File(availablePath));
                compress.add(uri);
            }
            if (compress.size() == 0) {
                listener.onCall(list);
                return;
            }
            Luban.with(context)
                    .load(compress)
                    .ignoreBy(100)
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path);
                        }
                    })
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            int indexOf = filePath.lastIndexOf(".");
                            String postfix = indexOf != -1 ? filePath.substring(indexOf) : ".jpg";
                            return DateUtils.getCreateFileName("CMP_") + postfix;
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(int index, File compressFile) {
                            LocalMedia media = list.get(index);
                            if (compressFile.exists() && !TextUtils.isEmpty(compressFile.getAbsolutePath())) {
                                media.setCompressed(true);
                                media.setCompressPath(compressFile.getAbsolutePath());
                                media.setSandboxPath(SdkVersionUtils.isQ() ? media.getCompressPath() : null);
                            }
                            if (index == list.size() - 1) {
                                listener.onCall(list);
                            }
                        }

                        @Override
                        public void onError(int index, Throwable e) {
                            if (index != -1) {
                                LocalMedia media = list.get(index);
                                media.setCompressed(false);
                                media.setCompressPath(null);
                                media.setSandboxPath(null);
                                if (index == list.size() - 1) {
                                    listener.onCall(list);
                                }
                            }
                        }
                    }).launch();
        }

    }


    /**
     * 压缩引擎
     *
     * @return
     */
    private ImageFileCompressEngine getCompressFileEngine() {
        return new ImageFileCompressEngine() ;
    }

    /**
     * 压缩引擎
     *
     * @return
     */
    @Deprecated
    private ImageCompressEngine getCompressEngine() {
        return new ImageCompressEngine() ;
    }

    /**
     * 裁剪引擎
     *
     * @return
     */
    private ImageFileCropEngine getCropFileEngine() {
        return  new ImageFileCropEngine();
    }

    /**
     * 裁剪引擎
     *
     * @return
     */
    private ImageCropEngine getCropEngine() {
        return  new ImageCropEngine() ;
    }

    /**
     * 自定义相机事件
     *
     * @return
     */
    private OnCameraInterceptListener getCustomCameraEvent() {
        return false ? new PostBuyActivity.MeOnCameraInterceptListener() : null;
    }

    /**
     * 自定义数据加载器
     *
     * @return
     */
    private ExtendLoaderEngine getExtendLoaderEngine() {
        return new PostBuyActivity.MeExtendLoaderEngine();
    }

    /**
     * 注入自定义布局
     *
     * @return
     */
    private OnInjectLayoutResourceListener getInjectLayoutResource() {
        return  null;
    }


    /**
     * 权限拒绝后回调
     *
     * @return
     */
    private OnPermissionDeniedListener getPermissionDeniedListener() {
        return  null;
    }


    /**
     * 添加权限说明
     *
     * @param viewGroup
     * @param permissionArray
     */
    private static void addPermissionDescription(boolean isHasSimpleXCamera, ViewGroup viewGroup, String[] permissionArray) {
        int dp10 = DensityUtil.dip2px(viewGroup.getContext(), 10);
        int dp15 = DensityUtil.dip2px(viewGroup.getContext(), 15);
        MediumBoldTextView view = new MediumBoldTextView(viewGroup.getContext());
        view.setTag(TAG_EXPLAIN_VIEW);
        view.setTextSize(14);
        view.setTextColor(Color.parseColor("#333333"));
        view.setPadding(dp10, dp15, dp10, dp15);

        String title;
        String explain;

        if (TextUtils.equals(permissionArray[0], PermissionConfig.CAMERA[0])) {
            title = "相机权限使用说明";
            explain = "相机权限使用说明\n用户app用于拍照/录视频";
        } else if (TextUtils.equals(permissionArray[0], Manifest.permission.RECORD_AUDIO)) {
            if (isHasSimpleXCamera) {
                title = "麦克风权限使用说明";
                explain = "麦克风权限使用说明\n用户app用于录视频时采集声音";
            } else {
                title = "录音权限使用说明";
                explain = "录音权限使用说明\n用户app用于采集声音";
            }
        } else {
            title = "存储权限使用说明";
            explain = "存储权限使用说明\n用户app写入/下载/保存/读取/修改/删除图片、视频、文件等信息";
        }
        int startIndex = 0;
        int endOf = startIndex + title.length();
        SpannableStringBuilder builder = new SpannableStringBuilder(explain);
        builder.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(viewGroup.getContext(), 16)), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(0xFF333333), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.setText(builder);
        view.setBackground(ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.ps_demo_permission_desc_bg));

        if (isHasSimpleXCamera) {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = DensityUtil.getStatusBarHeight(viewGroup.getContext());
            layoutParams.leftMargin = dp10;
            layoutParams.rightMargin = dp10;
            viewGroup.addView(view, layoutParams);
        } else {
            ConstraintLayout.LayoutParams layoutParams =
                    new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topToBottom = R.id.title_bar;
            layoutParams.leftToLeft = ConstraintSet.PARENT_ID;
            layoutParams.leftMargin = dp10;
            layoutParams.rightMargin = dp10;
            viewGroup.addView(view, layoutParams);
        }
    }

    /**
     * 移除权限说明
     *
     * @param viewGroup
     */
    private static void removePermissionDescription(ViewGroup viewGroup) {
        View tagExplainView = viewGroup.findViewWithTag(TAG_EXPLAIN_VIEW);
        viewGroup.removeView(tagExplainView);
    }


    /**
     * 拦截自定义提示
     */
    private static class MeOnSelectLimitTipsListener implements OnSelectLimitTipsListener {

        @Override
        public boolean onSelectLimitTips(Context context, @Nullable LocalMedia media, PictureSelectionConfig config, int limitType) {
            if (limitType == SelectLimitType.SELECT_MIN_SELECT_LIMIT) {
                ToastUtils.showToast(context, "图片最少不能低于" + config.minSelectNum + "张");
                return true;
            } else if (limitType == SelectLimitType.SELECT_MIN_VIDEO_SELECT_LIMIT) {
                ToastUtils.showToast(context, "视频最少不能低于" + config.minVideoSelectNum + "个");
                return true;
            } else if (limitType == SelectLimitType.SELECT_MIN_AUDIO_SELECT_LIMIT) {
                ToastUtils.showToast(context, "音频最少不能低于" + config.minAudioSelectNum + "个");
                return true;
            }
            return false;
        }
    }


    /**
     * 自定义数据加载器
     */
    private class MeExtendLoaderEngine implements ExtendLoaderEngine {

        @Override
        public void loadAllAlbumData(Context context,
                                     OnQueryAllAlbumListener<LocalMediaFolder> query) {
            LocalMediaFolder folder = SandboxFileLoader
                    .loadInAppSandboxFolderFile(context, getSandboxPath());
            List<LocalMediaFolder> folders = new ArrayList<>();
            folders.add(folder);
            query.onComplete(folders);
        }

        @Override
        public void loadOnlyInAppDirAllMediaData(Context context,
                                                 OnQueryAlbumListener<LocalMediaFolder> query) {
            LocalMediaFolder folder = SandboxFileLoader
                    .loadInAppSandboxFolderFile(context, getSandboxPath());
            query.onComplete(folder);
        }

        @Override
        public void loadFirstPageMediaData(Context context, long bucketId, int page, int pageSize, OnQueryDataResultListener<LocalMedia> query) {
            LocalMediaFolder folder = SandboxFileLoader
                    .loadInAppSandboxFolderFile(context, getSandboxPath());
            query.onComplete(folder.getData(), false);
        }

        @Override
        public void loadMoreMediaData(Context context, long bucketId, int page, int limit, int pageSize, OnQueryDataResultListener<LocalMedia> query) {

        }
    }


    /**
     * 录音回调事件
     */
    private static class MeOnRecordAudioInterceptListener implements OnRecordAudioInterceptListener {

        @Override
        public void onRecordAudio(Fragment fragment, int requestCode) {
            String[] recordAudio = {Manifest.permission.RECORD_AUDIO};
            if (PermissionChecker.isCheckSelfPermission(fragment.getContext(), recordAudio)) {
                startRecordSoundAction(fragment, requestCode);
            } else {
                addPermissionDescription(false, (ViewGroup) fragment.requireView(), recordAudio);
                PermissionChecker.getInstance().requestPermissions(fragment,
                        new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionResultCallback() {
                            @Override
                            public void onGranted() {
                                removePermissionDescription((ViewGroup) fragment.requireView());
                                startRecordSoundAction(fragment, requestCode);
                            }

                            @Override
                            public void onDenied() {
                                removePermissionDescription((ViewGroup) fragment.requireView());
                            }
                        });
            }
        }
    }

    /**
     * 启动录音意图
     *
     * @param fragment
     * @param requestCode
     */
    private static void startRecordSoundAction(Fragment fragment, int requestCode) {
        Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (recordAudioIntent.resolveActivity(fragment.requireActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(recordAudioIntent, requestCode);
        } else {
            ToastUtils.showToast(fragment.getContext(), "The system is missing a recording component");
        }
    }

    /**
     * 自定义拍照
     */
    private class MeOnCameraInterceptListener implements OnCameraInterceptListener {

        @Override
        public void openCamera(Fragment fragment, int cameraMode, int requestCode) {
            SimpleCameraX camera = SimpleCameraX.of();
            camera.isAutoRotation(true);
            camera.setCameraMode(cameraMode);
            camera.setVideoFrameRate(25);
            camera.setVideoBitRate(3 * 1024 * 1024);
            camera.isDisplayRecordChangeTime(true);
            camera.isManualFocusCameraPreview(false);
            camera.isZoomCameraPreview(false);
            camera.setPermissionDeniedListener(null);
            camera.setPermissionDescriptionListener(null);
            camera.setImageEngine(new CameraImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    Glide.with(context).load(url).into(imageView);
                }
            });
            camera.start(fragment.requireActivity(), fragment, requestCode);
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

    /**
     * 自定义裁剪
     */
    private class ImageCropEngine implements CropEngine {

        @Override
        public void onStartCrop(Fragment fragment, LocalMedia currentLocalMedia,
                                ArrayList<LocalMedia> dataSource, int requestCode) {
            String currentCropPath = currentLocalMedia.getAvailablePath();
            Uri inputUri;
            if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(currentCropPath)) {
                inputUri = Uri.parse(currentCropPath);
            } else {
                inputUri = Uri.fromFile(new File(currentCropPath));
            }
            String fileName = DateUtils.getCreateFileName("CROP_") + ".jpg";
            Uri destinationUri = Uri.fromFile(new File(getSandboxPath(), fileName));
            UCrop.Options options = buildOptions();
            ArrayList<String> dataCropSource = new ArrayList<>();
            for (int i = 0; i < dataSource.size(); i++) {
                LocalMedia media = dataSource.get(i);
                dataCropSource.add(media.getAvailablePath());
            }
            UCrop uCrop = UCrop.of(inputUri, destinationUri, dataCropSource);
            //options.setMultipleCropAspectRatio(buildAspectRatios(dataSource.size()));
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
                }
            });
            uCrop.start(fragment.requireActivity(), fragment, requestCode);
        }
    }

    /**
     * 图片选择
     * @param model
     */
    private void forSystemResult(PictureSelectionSystemModel model) {
        if (false) {
            switch (resultMode) {
                case ACTIVITY_RESULT:
                    model.forSystemResultActivity(PictureConfig.REQUEST_CAMERA);
                    break;
                case CALLBACK_RESULT:
                    model.forSystemResultActivity(new PostBuyActivity.MeOnResultCallbackListener());
                    break;
                default:
                    model.forSystemResultActivity(launcherResult);
                    break;
            }
        } else {
            if (resultMode == CALLBACK_RESULT) {
                model.forSystemResult(new PostBuyActivity.MeOnResultCallbackListener());
            } else {
                model.forSystemResult();
            }
        }
    }

    /**
     * 图片选择
     * @param model
     */
    private void forSelectResult(PictureSelectionModel model) {
        switch (resultMode) {
            case ACTIVITY_RESULT:
                model.forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case CALLBACK_RESULT:
                model.forResult(new PostBuyActivity.MeOnResultCallbackListener());
                break;
            default:
                model.forResult(launcherResult);
                break;
        }
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    private UCrop.Options buildOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(!false);
        options.setFreeStyleCropEnabled(false);
        options.setShowCropFrame(false);
        options.setShowCropGrid(false);
        options.setCircleDimmedLayer(false);
        options.withAspectRatio(aspect_ratio_x, aspect_ratio_y);
        options.setCropOutputPathDir(getSandboxPath());
        options.isCropDragSmoothToCenter(false);
        options.setSkipCropMimeType(getNotSupportCrop());
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
                options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.app_color_grey));
                options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.app_color_grey));
            }
            TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
            if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleTextColor())) {
                options.setToolbarWidgetColor(titleBarStyle.getTitleTextColor());
            } else {
                options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        } else {
            options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.app_color_grey));
            options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.app_color_grey));
            options.setToolbarWidgetColor(ContextCompat.getColor(getContext(), R.color.app_color_grey));
        }
        return options;
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private String getVideoThumbnailDir() {
        File externalFilesDir = getContext().getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Thumbnail");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 处理视频缩略图
     */
    private OnVideoThumbnailEventListener getVideoThumbnailEventListener() {
        return  new PostBuyActivity.MeOnVideoThumbnailEventListener(getVideoThumbnailDir()) ;
    }

    /**
     * 处理视频缩略图
     */
    private static class MeOnVideoThumbnailEventListener implements OnVideoThumbnailEventListener {
        private final String targetPath;

        public MeOnVideoThumbnailEventListener(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public void onVideoThumbnail(Context context, String videoPath, OnKeyValueResultCallbackListener call) {
            Glide.with(context).asBitmap().sizeMultiplier(0.6F).load(videoPath).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    FileOutputStream fos = null;
                    String result = null;
                    try {
                        File targetFile = new File(targetPath, "thumbnails_" + System.currentTimeMillis() + ".jpg");
                        fos = new FileOutputStream(targetFile);
                        fos.write(stream.toByteArray());
                        fos.flush();
                        result = targetFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        PictureFileUtils.close(fos);
                        PictureFileUtils.close(stream);
                    }
                    if (call != null) {
                        call.onCallback(videoPath, result);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    if (call != null) {
                        call.onCallback(videoPath, "");
                    }
                }
            });
        }
    }

    /**
     * 图片选择
     * @param model
     */
    private void forOnlyCameraResult(PictureSelectionCameraModel model) {
        if (false) {
            switch (resultMode) {
                case ACTIVITY_RESULT:
                    model.forResultActivity(PictureConfig.REQUEST_CAMERA);
                    break;
                case CALLBACK_RESULT:
                    model.forResultActivity(new PostBuyActivity.MeOnResultCallbackListener());
                    break;
                default:
                    model.forResultActivity(launcherResult);
                    break;
            }
        } else {
            if (resultMode == CALLBACK_RESULT) {
                model.forResult(new PostBuyActivity.MeOnResultCallbackListener());
            } else {
                model.forResult();
            }
        }
    }

    public Context getContext() {
        return this;
    }
}