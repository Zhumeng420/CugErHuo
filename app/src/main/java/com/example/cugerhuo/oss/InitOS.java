package com.example.cugerhuo.oss;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.example.cugerhuo.access.util.OssSecret;

import java.util.List;

/**
 *初始化对象存储
 * @author zhumeng,shilihao
 * @time 2023/4/7/ 14：55,2023/4/7/ 20：13
 */
public class InitOS {
    /**
     * 创建 InitOs 的一个对象
     */

    private static InitOS instance = null;
    /**
     *  让构造函数为 private，这样该类就不会被实例化
     */

    private InitOS(Context context)
    {
        List<String> message=OssSecret.getOss();
        accessKeyId=message.get(0);
        accessKeySecret=message.get(1);
        securityToken=message.get(2);
        credentialProvider=new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        oss=new OSSClient(context, endpoint, credentialProvider);
    }
    /**
     *  获取唯一可用的对象
     */
    public static InitOS getInstance(Context context){
        if(instance==null)
        {
            instance=new InitOS(context);
        }

        return instance;
    }
    public static OSSClient getossclient()
    {

        return instance.oss;
    }
    public String get(){return accessKeySecret;};
    /**
     * yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
     */

    String endpoint = "oss-cn-beijing.aliyuncs.com";
    /**
     *  // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
     */

    String accessKeyId = null;
    String accessKeySecret = null;
    /**
     * // 从STS服务获取的安全令牌（SecurityToken）。
     */

    String securityToken = null;
    OSSCredentialProvider credentialProvider = null;
    /**
     *     // 创建OSSClient实例。
     */

    OSSClient oss = null;
}
