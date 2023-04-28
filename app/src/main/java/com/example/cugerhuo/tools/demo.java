package com.example.cugerhuo.tools;

import android.net.Uri;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CreateBucketRequest;
import com.alibaba.sdk.android.oss.model.CreateBucketResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.example.cugerhuo.oss.InitOS;

import java.util.Date;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * 链路追踪demo、接口调用demo等
 * @author 朱萌、施立豪
 */
public class demo {
    void demooo(){
        Tracer tracer = GlobalTracer.get();
        // 创建spann
        Span span = tracer.buildSpan("流程").withTag("函数：", "追踪").start();
        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
            // 业务逻辑写这里

        } catch (Exception e) {
            TracingHelper.onError(e, span);
            throw e;
        } finally {
            span.finish();
        }
    }
    /**
     *
     *对象存储上本地传文件 demo
     * @author 施立豪
     * @time 2023/4/8
     */
    public static void  up(Uri imageUri)
    {
        // 构造上传请求。
// 依次填写Bucket名称（例如examplebucket）、Object完整路径（例如exampledir/exampleobject.txt）和本地文件完整路径（例如/storage/emulated/0/oss/examplefile.txt）。
// Object完整路径中不能包含Bucket名称。
        String datePath=new Date().toString();
        String fileName=datePath+imageUri.toString();
        PutObjectRequest put = new PutObjectRequest("cugerhuo", fileName, imageUri);
// 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSClient oss= InitOS.getOssClient();
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
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
// 取消上传任务。
// task.cancel();
// 等待上传任务完成。
// task.waitUntilFinished();
    }
    public  void createBucket()

    {
        // 构建创建Bucket的请求。
// 填写Bucket名称。
        CreateBucketRequest createBucketRequest = new CreateBucketRequest("userimagebucket");
// 指定Bucket所在的地域为华东1（杭州）。
        createBucketRequest.setLocationConstraint("oss-cn-beijing");
// 设置Bucket的读写权限ACL。
// CreateBucketRequest.setBucketACL(CannedAccessControlList.Private);
// 指定Bucket的存储类型。
// CreateBucketRequest.setBucketStorageClass(StorageClass.Standard);
        OSSClient oss=InitOS.getOssClient();
// 异步创建存储空间。
        OSSAsyncTask createTask = oss.asyncCreateBucket(createBucketRequest, new OSSCompletedCallback<CreateBucketRequest, CreateBucketResult>() {
            @Override
            public void onSuccess(CreateBucketRequest request, CreateBucketResult result) {
                Log.d("asyncCreateBucket", "Success");
            }
            @Override
            public void onFailure(CreateBucketRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常。
                if (clientException != null) {
                    // 本地异常如网络异常等。
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    /**
     * 操作redis的demo，以获取用户的id，个签，用户名，头像url为例
     * @author 施立豪
     * @time 2023/4/10
     */
    public void redisOperate(){
        //建立连接对象
//                LettuceBaseCase lettuce=new LettuceBaseCase();
        //获取连接
//                RedisCommands<String, String> con=lettuce.getSyncConnection();
        //通过连接调用查询
//                PartUserInfo part= UserInfoOperate.getInfoFromRedis(con,5,StartSplashActivity.this);
//                System.out.println(part.getImageUrl());
        //关闭连接
//                lettuce.close();
    }
    /**
     * 操作redis的demo，以获取用户的id，个签，用户名，头像url为例
     * @author 施立豪
     * @time 2023/4/10
     */
    public void imageGet(){
        /**
         * 获取关注对象的头像
         * @author 施立豪
         * @time 2023/4/12
         */
        //getFocusInfo 列表
//        for(PartUserInfo i :getFocusInfo)
//        {
//            String url=i.getImageUrl();
//            String newUrl=getSandBoxPath(ConcernActivity.this)+url;
//            if(!"".equals(i.getImageUrl())&&i.getImageUrl()!=null)
//            {
//                OssOperate.downLoad(i.getImageUrl(),newUrl);
//                i.setImageUrl(newUrl);
//                System.out.println(newUrl+i.getImageUrl());
//            }
//        }

    }

    /**
     * 下载图片并下载完成更新UI示例，放在主线程，复制被注释的代码至需要更新UI的地方，更换代码中更新UI的函数，和url地址
     * @author 施立豪
     * @time 2023/4/13
     */
    public void downLoadImageSync()
    {
//        /**
//         * 异步更新头像,并实时更新
//         */
//        OSSClient oss= InitOS.getOssClient();
//        /**
//         * 获取oss路径
//         */
//        String url=partUserInfo.get(position).getImageUrl();
//        /**
//         * 获取本地保存路径
//         */
//        String newUrl=getSandBoxPath(context)+url;
//        File f = new File(newUrl);
//        if(!f.exists())
//        {
//            /**
//             * 构建oss请求
//             */
//            GetObjectRequest get = new GetObjectRequest("cugerhuo", url);
//            /**
//             * 异步任务
//             */
//            oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
//                /**
//                 * 下载成功
//                 * @param request
//                 * @param result
//                 */
//                @Override
//                public void onSuccess(GetObjectRequest request, GetObjectResult result) {
//                    // 开始读取数据。
//                    long length = result.getContentLength();
//                    if (length > 0) {
//                        byte[] buffer = new byte[(int) length];
//                        int readCount = 0;
//                        while (readCount < length) {
//                            try{
//                                readCount += result.getObjectContent().read(buffer, readCount, (int) length - readCount);
//                            }catch (Exception e){
//                                OSSLog.logInfo(e.toString());
//                            }
//                        }
//                        // 将下载后的文件存放在指定的本地路径，例如D:\\localpath\\exampleobject.jpg。
//                        try {
//                            FileOutputStream fout = new FileOutputStream(newUrl);
//                            fout.write(buffer);
//                            fout.close();
//                            /**
//                             * 下载完成，填写更新逻辑
//                             */
//                            holder.user_concern_img.setImageURI(Uri.fromFile(new File(newUrl)));
//                        } catch (Exception e) {
//                            OSSLog.logInfo(e.toString());
//                        }
//                    }
//                }
//                @Override
//                public void onFailure(GetObjectRequest request, ClientException clientException,
//                                      ServiceException serviceException)  {
//                    Log.e(TAG,"oss下载文件失败");
//                }
//            });
//        }
//        else
//        {
//            holder.user_concern_img.setImageURI(Uri.fromFile(new File(newUrl)));
//
//        }

    }

}
