package com.example.cugerhuo.ObjectStorege;

import static com.nirvana.tools.core.ComponentSdkCore.getApplicationContext;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;

/**
 *初始化对象存储
 * @author zhumeng
 * @time 2023/4/7/ 14：55
 */
public class InitOS {
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    String endpoint = "https://cugerhuo.oss-cn-beijing.aliyuncs.com";
    // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
    String accessKeyId = "yourAccessKeyId";
    String accessKeySecret = "yourAccessKeySecret";
    // 从STS服务获取的安全令牌（SecurityToken）。
    String securityToken = "yourSecurityToken";

    OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
    // 创建OSSClient实例。
    OSSClient oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
}
