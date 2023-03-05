//package com.example.cugerhuo.loginUtils;
//
//// This file is auto-generated, don't edit it. Thanks.
//
//import com.aliyun.auth.credentials.Credential;
//import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
//import com.aliyun.core.http.HttpClient;
//import com.aliyun.core.http.HttpMethod;
//import com.aliyun.core.http.ProxyOptions;
//import com.aliyun.httpcomponent.httpclient.ApacheAsyncHttpClientBuilder;
//import com.aliyun.sdk.service.dypnsapi20170525.models.*;
//import com.aliyun.sdk.service.dypnsapi20170525.*;
//import com.google.gson.Gson;
//import darabonba.core.RequestConfiguration;
//import darabonba.core.client.ClientOverrideConfiguration;
//import darabonba.core.utils.CommonUtil;
//import darabonba.core.TeaPair;
//
////import javax.net.ssl.KeyManager;
////import javax.net.ssl.X509TrustManager;
//import java.net.InetSocketAddress;
//import java.time.Duration;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//
//public class GetMobile {
//    public static void main(String[] args) throws Exception {
//
//        // HttpClient Configuration
//        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
//                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
//                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
//                .maxConnections(128) // Set the connection pool size
//                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
//                // Configure the proxy
//                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
//                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
//                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
//                .x509TrustManagers(new X509TrustManager[]{})
//                .keyManagers(new KeyManager[]{})
//                .ignoreSSL(false)
//                .build();*/
//
//        // Configure Credentials authentication information, including ak, secret, token
//        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
//                .accessKeyId("<your-accessKeyId>")
//                .accessKeySecret("<your-accessKeySecret>")
//                //.securityToken("<your-token>") // use STS token
//                .build());
//
//        // Configure the Client
//        AsyncClient client = AsyncClient.builder()
//                .region("cn-hangzhou") // Region ID
//                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
//                .credentialsProvider(provider)
//                //.serviceConfiguration(Configuration.create()) // Service-level configuration
//                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
//                .overrideConfiguration(
//                        ClientOverrideConfiguration.create()
//                                .setEndpointOverride("dypnsapi.aliyuncs.com")
//                        //.setConnectTimeout(Duration.ofSeconds(30))
//                )
//                .build();
//
//        // Parameter settings for API request
//        GetMobileRequest getMobileRequest = GetMobileRequest.builder()
//                .accessToken("eyJjIjoiNkxhRDM2dUtpdHM1VHcrUjQ0NGd6MWlVZ1QxOWo0UGNZZmFWZUhFYVhFQlJJdDYrTHpiSHh6Y3B5Rk1ENTNjZXYxQ1dMbmhsRlNuWFxueTlUbTlvMm1IaFBiekc2ejdoZUI5ams5YVVsY0YyVmlyK0FHVGZicllHU2hIY3NHbG9hV1B4Z2ltUERRWGs2ZElmbWJCZUpJMnNcL1lcbk1SQTA1NHUzVVo1ZDFwMStCdVloV1pScjVLOCtcL0V5ajFvaEhwZkozdVgwdkVEakRSVDhKNEd0U0haQlJrT1wva25wWXRZbnZhVkNSSFxub1E0NHlkTG5KbkdLVm9ldWhLdTd1aWowd280Q2hiN2tnM3VFV3A2d2E1WDFJMHFIVEFkV2Y4RHFOc3dNcHNNQlwvWDhHYVp6U0lFYzZcbjZjZFpvVzg0WlpJQTJxNVJNVEZBaytiNEs0andtOHVtYVdWaExtVldcL3U3QWR3Y29sdk5XT0xnTkhUU2xVc0VXVGZ4S3FPMTE0ejVEXG5JTEVBZklmVDhsRk4rb3VqS3I1Ym9BUWdjb1wvWXg1Zk5Kb0hxMHQ0U2ZsVUN3cUZta1FDS3h4Z3VMN2w2VEVJTDF0U05JdEQ4REJsY1xucDhKQVJ2RVV4ZENZUUtvdFJIbzZMV1IwTlIxNmcyaWRMRlFnVmNpMnpLZFloQTlGMEp4WEhoZ3BIRjhSZTZoY3d2djY1c2RLYVJSOVxuaW93VmswU0VxdUJxWlBSNDJ1R21KYVwvTGpubHRFb3JpYW5LaEVqNFVzR3lidmxMVllBVGJnc1Z5dkdCN2p0cEdYQ2NtdWkrVTRCbXJcbnNEZ21WUFBpYWJZdW5xcG05cWUyREVJcWtsRE5SV3VpYUVQc2F6UHh5NW5wMVhCRTBTUldHQWJ0cmJldEJVS3pvUWxnRWMrTTBqOD1cbiIsImsiOiJFQ1BZa0xHR1E5NnFpa0tlWjRMaDlzSUVNdTBiMmNrWFNtdU1qMVc5VTE0MjhhUldueWhRYzJLaVRnRjVZUGtpeHdJNnpTMHhlYytqTVVqUVNVcmlEM2FjbGVuSmVWNTcwVWRxMlwvK3Y2amcweko3bmVreG80NXVCcEJRbGFEVWpJV2R3U1UyQ0M2dGhoQWhGUFdVRlpSdVcxYnFcL1wvd1B4TGgwRWg3WStFQWVcL0VkNEFEUThNVHVUY1FaNjhkR2VkbVM5dkMyZTJoZmVCd0V1d3MxeGtrejFiWUdyTWJmdFJ4NDJHNTFORDJOXC8yVUxBcUhvdlwvQU5Mb2NLZW9tYTlHZXlWZ3lZZ1l5cHlyZ2NpUmU2VzZxaUZVMitDVXF6MnRFd1dlU2pXRWpOZGpqODlISVA5RWt3dU1TZFJ5V2ZUOGVSMFJvZk9oM1YyZk5nNGQzc3VvMFE9PSIsIm8iOiJBbmRyb2lkIn0=")
//                // Request-level configuration rewrite, can set Http request parameters, etc.
//                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
//                .build();
//
//        // Asynchronously get the return value of the API request
//        CompletableFuture<GetMobileResponse> response = client.getMobile(getMobileRequest);
//        // Synchronously get the return value of the API request
//        GetMobileResponse resp = response.get();
//        System.out.println(new Gson().toJson(resp));
//        // Asynchronous processing of return values
//        /*response.thenAccept(resp -> {
//            System.out.println(new Gson().toJson(resp));
//        }).exceptionally(throwable -> { // Handling exceptions
//            System.out.println(throwable.getMessage());
//            return null;
//        });*/
//
//        // Finally, close the client
//        client.close();
//    }
//
//}
