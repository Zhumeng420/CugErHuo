package com.example.cugerhuo.tools;

import android.content.Context;
import android.util.Base64;

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

import java.nio.charset.Charset;

public class TextAudit {
    public static void textAudit(Context context)
    {

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
        CIService ciService=new CIService(context,serviceConfig,myCredentialProvider);
        // 存储桶名称，格式为 BucketName-APPID
        String bucket = "cug-erhuo-1314485188";
        // 对象键，是对象在 COS 上的完整路径，如果带目录的话，格式为 "dir1/object1"
        String cosPath = "exampleobject.txt";
        //文本的链接地址,Object 和 Url 只能选择其中一种
        String url = "https://myqcloud.com/%205text.txt";
        //当传入的内容为纯文本信息，需要先经过 base64 编码，文本编码前的原文长度不能超过10000个 utf8 编码字符。若超出长度限制，接口将会报错。
        String content = Base64.encodeToString(("澳门赌场" +
                "，").getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP);
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
                    }
                    @Override
                    public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
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
                if (clientException != null) {
                    clientException.printStackTrace();
                } else {
                    serviceException.printStackTrace();
                }
            }
        });
        //结果

        // 审核任务的 ID


    }
}
