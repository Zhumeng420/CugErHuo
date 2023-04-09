package com.example.cugerhuo.tools;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.cugerhuo.ObjectStorege.InitOS;

import java.io.FileOutputStream;

/**
 * 对象存储操作类
 * @author 施立豪
 * @time 2023/4/8
 */
public class OssOperate {
    /**
     * 对象存储上次图片类，Uri为图片的Uri
     * @param fileName 存储的文件名
     * @param imageUri 图片Uri
     * @return 返回对象存储中保存的文件名，此文件名需要保存，以方便后续获取该图片，如果存储失败，则返回空字符串，接收结果时进行判断
     */
    public static boolean  Up(String fileName,Uri imageUri)
    {
        // 构造上传请求。
        // 依次填写Bucket名称（例如examplebucket）、Object完整路径（例如exampledir/exampleobject.txt）和本地文件完整路径（例如/storage/emulated/0/oss/examplefile.txt）。
        // Object完整路径中不能包含Bucket名称。

        /**
         * 使用final []表示，便于在task内部函数进行修改
         */
        final boolean[] myResult = {false};

        PutObjectRequest put = new PutObjectRequest("cugerhuo",fileName, imageUri);
        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSClient oss= InitOS.getOSSClient();
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                myResult[0] =true;
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                /**
                 * 文件名设为空，以与成功做区分
                 */

                if (clientExcepion != null) {
                    // 客户端异常，例如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务端异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        return myResult[0];
    }

    /**
     * oss下载文件到本地
     * @param filePath  oss存储路径
     * @param outPutPath  下载到本地的位置
     * @return      是否下载成功
     */
    public static boolean DownLoad(String filePath,String outPutPath)
    {

        // 构造下载文件请求。
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径，Object完整路径中不能包含Bucket名称。
        GetObjectRequest get = new GetObjectRequest("cugerhuo", filePath);
        /**
         * 获取oss实例
         */
        OSSClient oss= InitOS.getOSSClient();
        /**
         * 接收结果变量
         */
        final boolean[] IsDownloaded = {false};
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
                        FileOutputStream fout = new FileOutputStream(outPutPath);
                        fout.write(buffer);
                        fout.close();
                        IsDownloaded[0] =true;
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
        return IsDownloaded[0];
    }
}
