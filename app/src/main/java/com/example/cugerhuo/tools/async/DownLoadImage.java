package com.example.cugerhuo.tools.async;

import static android.content.ContentValues.TAG;
import static com.example.cugerhuo.access.SetGlobalIDandUrl.getSandBoxPath;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.example.cugerhuo.oss.InitOS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 异步下载文件
 * @author 施立豪
 * @time 2023/4/13
 */
public class DownLoadImage extends AsyncTask<String,Integer, Boolean> implements DownLoadImageInterface {
    private RoundedImageView imageView=null;
    private String url;
    private String newUrl;
    private Context context;
    public DownLoadImage(RoundedImageView view,String url,Context context)
    {
        imageView=view;this.url=url;this.context=context;
    }
    @Override
    public Boolean doInBackground(String... inputs) {
        String newUrl=getSandBoxPath(context)+url;
        if(!"".equals(url)&&url!=null)
        {
            OSSClient oss= InitOS.getOssClient();
            GetObjectRequest get = new GetObjectRequest("cugerhuo", url);

            /**
             * 接收结果变量
             */
            final boolean[] isDownloaded = {false};

            oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                @Override
                public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                    // 开始读取数据。
                    long length = result.getContentLength();
                    if (length > 0) {
                        byte[] buffer = new byte[(int) length];
                        int readCount = 0;
                        while (readCount < length) {
                            try{
                                readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
                            }catch (Exception e){
                                OSSLog.logInfo(e.toString());
                            }
                        }
                        // 将下载后的文件存放在指定的本地路径，例如D:\\localpath\\exampleobject.jpg。
                        try {
                            FileOutputStream fout = new FileOutputStream(newUrl);
                            fout.write(buffer);
                            fout.close();
                            isDownloaded[0] =true;
                            return;
                        } catch (Exception e) {
                            OSSLog.logInfo(e.toString());
                        }
                    }
                }

                @Override
                public void onFailure(GetObjectRequest request, ClientException clientException,
                                      ServiceException serviceException)  {
                    Log.e(TAG,"oss下载文件失败");
                }
            });

            this.newUrl=newUrl;
        }
        return true;
    }
    @Override
    public void onProgressUpdate(Integer... progress) {
    }

    @Override
    public void onPostExecute(Boolean result) {
        if(newUrl!=null&&!"".equals(newUrl)){
        imageView.setImageURI(Uri.fromFile(new File(newUrl)));}
    }


}
