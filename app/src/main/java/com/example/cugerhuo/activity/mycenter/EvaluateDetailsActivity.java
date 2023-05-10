package com.example.cugerhuo.activity.mycenter;
import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;
import static com.mobile.auth.gatewayauth.utils.ReflectionUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cugerhuo.R;
import com.example.cugerhuo.access.Commodity;
import com.example.cugerhuo.access.commerce.Commerce;
import com.example.cugerhuo.access.evaluate.CommodityEvaluateOperate;
import com.example.cugerhuo.access.evaluate.Evaluation;
import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.activity.OtherPeopleActivity;
import com.example.cugerhuo.activity.adapter.RecyclerViewEvaluateAdapter;
import com.example.cugerhuo.oss.InitOS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品评价页
 * @author carollkarry
 * @time 2023/5/9
 */
public class EvaluateDetailsActivity extends AppCompatActivity {
  private Button goodComment;
  private Button badComment;
  private Button middleComment;
    private Commodity commodities;
    private Commerce commerce;
    private ImageView commodityImg;
    private TextView commodityTitle;
    private TextView commodityPrice;
    private EditText editUserSign;
    private Button publishEvaluate;

    private int score;
    private String context="";
    private final MyHandler MyHandler =new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_details);
        Intent intent =getIntent();
        commodities= (Commodity) intent.getSerializableExtra("eCommodityList");
        commerce= (Commerce) intent.getSerializableExtra("commerce");
        goodComment=findViewById(R.id.good_comment);
        commodityImg=findViewById(R.id.good_item_buy_img);
        commodityTitle=findViewById(R.id.good_item_buy_title);
        commodityPrice=findViewById(R.id.goods_item_buy_price);
        editUserSign=findViewById(R.id.editUserSign);
        publishEvaluate=findViewById(R.id.publish_evaluate);
        setCommodityInfo();

        //获取Drawable
        Drawable drawable = getResources().getDrawable(R.drawable.good_no_comment);

        //想改变高度就修改top或bottom,改变宽度就修改left或right.
        drawable.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪一侧,分别是左上右下
        goodComment.setCompoundDrawables(drawable, null,null, null);

        badComment=findViewById(R.id.bad_comment);

        Drawable drawable1 = getResources().getDrawable(R.drawable.bad_no_comment);
        //想改变高度就修改top或bottom,改变宽度就修改left或right.
        drawable1.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪一侧,分别是左上右下
        badComment.setCompoundDrawables(drawable1, null,null, null);

        middleComment=findViewById(R.id.middle_comment);
        Drawable drawable2 = getResources().getDrawable(R.drawable.middle_no_comment);
        //想改变高度就修改top或bottom,改变宽度就修改left或right.
        drawable2.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪一侧,分别是左上右下
        middleComment.setCompoundDrawables(drawable2, null,null, null);

        goodComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getResources().getDrawable(R.drawable.good_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                goodComment.setCompoundDrawables(drawable, null,null, null);

                Drawable drawable2 = getResources().getDrawable(R.drawable.middle_no_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable2.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                middleComment.setCompoundDrawables(drawable2, null,null, null);

                Drawable drawable1 = getResources().getDrawable(R.drawable.bad_no_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable1.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                badComment.setCompoundDrawables(drawable1, null,null, null);
                score=3;
            }

        });
        middleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getResources().getDrawable(R.drawable.good_no_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                goodComment.setCompoundDrawables(drawable, null,null, null);

                Drawable drawable2 = getResources().getDrawable(R.drawable.middle_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable2.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                middleComment.setCompoundDrawables(drawable2, null,null, null);

                Drawable drawable1 = getResources().getDrawable(R.drawable.bad_no_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable1.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                badComment.setCompoundDrawables(drawable1, null,null, null);
                score=2;
            }
        });
        badComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getResources().getDrawable(R.drawable.good_no_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                goodComment.setCompoundDrawables(drawable, null,null, null);

                Drawable drawable2 = getResources().getDrawable(R.drawable.middle_no_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable2.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                middleComment.setCompoundDrawables(drawable2, null,null, null);

                Drawable drawable1 = getResources().getDrawable(R.drawable.bad_comment);
                //想改变高度就修改top或bottom,改变宽度就修改left或right.
                drawable1.setBounds(0, 0, 50, 50);
                //设置图片在文字的哪一侧,分别是左上右下
                badComment.setCompoundDrawables(drawable1, null,null, null);
                score=1;
            }
        });

        publishEvaluate.setOnClickListener(this::onClickPublish);


    }

    public void onClickPublish(View view){
        Evaluation evaluation=new Evaluation();
       evaluation.setCommerid(commerce.getCommerceid());
       evaluation.setState(1);
       if(!editUserSign.getText().toString().equals("")){
           evaluation.setContent(editUserSign.getText().toString());
           evaluation.setScore(score);
           evaluation.setUserid(UserInfo.getid());
           Timestamp d = new Timestamp(System.currentTimeMillis());
           evaluation.setTime(d);
           new Thread(()->{
               Message msg = Message.obtain();
               msg.arg1 = 0;
               if(CommodityEvaluateOperate.updateEvlution(evaluation,EvaluateDetailsActivity.this)){
                  msg.arg1=1;
               }
               //4、发送消息
               MyHandler.sendMessage(msg);
           }).start();
       }
       else{
           Toast.makeText(this, "请输入评论！", Toast.LENGTH_SHORT).show();
       }
    }

    public void setCommodityInfo(){
        commodityTitle.setText(commodities.getDescription());
        commodityPrice.setText(String.valueOf(commodities.getPrice()));
        setCommodityImg();

    }

    public void setCommodityImg(){
        /**
         * 商品头像
         */
        String url1=commodities.getUrl1();

        if(url1!=null&&!"".equals(url1))
        {
            String []urls=url1.split(";");
            if(urls.length>0){
                url1=urls[0];}
            int length=urls.length;
            String result[]=new String[length];
            result[length-1]=urls[length-1];
            // 从后往前依次减去后面一个元素
            if(length>1){
                for (int i = length - 2; i >= 0; i--) {
                    String current = urls[i];
                    String next = result[i + 1];
                    int index = current.lastIndexOf(next);
                    if(index>0){
                        result[i ] = current.substring(0, index);}
                    else
                    {
                        result[i]=current;
                    }
                }}
// 将第一个元素赋值给结果数组
            url1=result[0];

        }
        /**
         * 异步加载头像
         */
        /**
         *  判断头像是否为空，如果为空则使用默认的头像进行显示
         */
        if (url1 != null && !"".equals(url1)) { /**@time 2023/4/26
         * @author 唐小莉
         * 异步更新头像,并实时更新
         */
            System.out.println("url11"+url1);
            OSSClient oss = InitOS.getOssClient();

            /**
             * 获取本地保存路径
             */
            String newUrl1 = getSandBoxPath(getApplicationContext()) + url1;
            System.out.println("imager2"+url1);
            File f = new File(newUrl1);
            if (!f.exists()) {
                /**
                 * 构建oss请求
                 */
                GetObjectRequest get = new GetObjectRequest("cugerhuo", url1);
                /**
                 * 异步任务
                 */
                oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                    /**
                     * 下载成功
                     *
                     * @param request
                     * @param result
                     */
                    @Override
                    public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                        // 开始读取数据。
                        long length = result.getContentLength();
                        if (length > 0) {
                            byte[] buffer = new byte[(int) length];
                            int readCount = 0;
                            while (readCount < length) {
                                try {
                                    readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
                                } catch (Exception e) {
                                    OSSLog.logInfo(e.toString());
                                }
                            }
                            // 将下载后的文件存放在指定的本地路径，例如D:\\localpath\\exampleobject.jpg。
                            try {
                                FileOutputStream fout = new FileOutputStream(newUrl1);
                                fout.write(buffer);
                                fout.close();
                                /**
                                 * 下载完成，填写更新逻辑
                                 */
                                /**
                                 * 设置商品图片圆角30度
                                 */
                                System.out.println("image1"+newUrl1);

                                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
                                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(newUrl1)))
                                        .apply(options)
                                        .into(commodityImg);
                            } catch (Exception e) {
                                OSSLog.logInfo(e.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(GetObjectRequest request, ClientException clientException,
                                          ServiceException serviceException) {
                        System.out.println("image3"+newUrl1);
                        Log.e(TAG, "oss下载文件失败");
                    }
                });
            } else {
                System.out.println("image2"+newUrl1);
                /**
                 * 设置商品图片圆角30度
                 */
                RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(30));
                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(newUrl1)))
                        .apply(options)
                        .into(commodityImg);
            }
        }
    }

    /**
     * 消息发送接收，异步更新UI
     * @author 施立豪
     * @time 2023/5/11
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                /**
                 * 更新关注数
                 */
                case 1:
                    Toast.makeText(EvaluateDetailsActivity.this, "评价成功！", Toast.LENGTH_SHORT).show();
                    finish();
                   // userFocus.setText(String.valueOf(msg.arg2));
                    break;
                default:
                    break;
            }
        }
    }
}