package com.example.cugerhuo.access;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cugerhuo.access.user.UserInfo;
import com.example.cugerhuo.access.user.UserInfoOperate;
import com.example.cugerhuo.access.user.UserOperate;
import com.example.cugerhuo.tools.GetFileNameUtil;
import com.example.cugerhuo.tools.OssOperate;
import com.example.cugerhuo.tools.TracingHelper;

import java.io.File;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * 进入应用OR登录后设置全局变量ID和头像url的函数
 * @author 施立豪
 * @time 2023/4/9
 */
public class SetGlobalIDandUrl {
    /**
     * 通过手机号获取ID和头像url，设置全局变量
     * @param phone 手机号
     * @param context 辅助操作
     * @return      true
     */
    public static boolean setByPhone(String phone, Context context)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 获取ID
                 */
                String phoneNumber=phone;
                Tracer tracer = GlobalTracer.get();
                // 创建spann
                int id=-1;
                Span span = tracer.buildSpan("通过phone启动获取用户ID流程").withTag("Oncreate函数：", "子追踪").start();
                try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                    // 调用接口
                    id= UserOperate.getId(phoneNumber, context);
                } catch (Exception e) {
                    TracingHelper.onError(e, span);
                    throw e;
                } finally {
                    span.finish();
                }
                /**
                 * 得到ID，用id获取图片url
                 */
                if(id!=-1)
                {
                    UserInfo.setID(id);
                    String imageUrl="";
                    Span span3 = tracer.buildSpan("通过id获取图片url").withTag("Oncreate函数：", "子追踪").start();
                    try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                        // 调用获取头像url接口
                        imageUrl= UserInfoOperate.getImage(id,context);
                    } catch (Exception e) {
                        TracingHelper.onError(e, span);
                        throw e;
                    } finally {
                        span.finish();
                    }
                    /**
                     * 得到url
                     */
                    if(!imageUrl.equals(""))
                    {
                        /**
                         * 获取本地存储图片路径
                         */
                        SharedPreferences imagePath=context.getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
                        String imageAbsolutePath=imagePath.getString("imagepath","");
                        String nativeName= GetFileNameUtil.GetFileName(imageAbsolutePath);
                        /**
                         * 如果本地和用户表中存储的文件名一致，则直接用本地的
                         */
                        if(imageUrl.equals(nativeName))
                        {
                            UserInfo.setUrl(imageAbsolutePath);
                        }
                        /**
                         * 否则下载oss的文件到本地，并修改本地存储的图片路径
                         */
                        else
                        {
                            /**
                             * 接收下载结果
                             */
                            boolean IsDownLoad=false;
                            Span span4 = tracer.buildSpan("缓存头像流程").withTag("onCreaate函数：", "子追踪").start();
                            try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                                // 调用下载接口
                                IsDownLoad= OssOperate.DownLoad(imageUrl, getSandBoxPath(context)+imageUrl);
                            } catch (Exception e) {
                                TracingHelper.onError(e, span);
                                throw e;
                            } finally {
                                span.finish();
                            }
                            if(IsDownLoad)
                                UserInfo.setUrl(getSandBoxPath(context)+imageUrl);
                            else
                            {
                                Log.e(TAG,"从oss缓存头像URL失败");
                            }
                            /**
                             * 设置全局URL，因为是异步下载图片，所以可能会直接忽略结果返回失败，因此直接默认会缓存成功
                             */
                            UserInfo.setUrl(getSandBoxPath(context)+imageUrl);

                        }
                    }
                    /**
                     * 未得到url
                     */
                    else{
                        Log.e(TAG,"Id获取头像URL失败");
                    }
                }
                else
                {
                    Log.e(TAG,"phone获取用户ID失败");

                }
            }
        }).start();
        return true;
    }
    /**
     * 通过qq openID获取ID和头像url，设置全局变量
     * @param qqId qq openID
     * @param context 辅助操作
     * @return      true
     */
    public static boolean setByQq(String qqId, Context context)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 获取本地qqId，通过qqId获取ID
                 * @author 施立豪
                 * @time 2023/4/9
                 */

                Tracer tracer = GlobalTracer.get();
                // 创建spann
                /**
                 * 接收返回结果
                 */
                int id=-1;
                Span span = tracer.buildSpan("通过qq启动获取用户ID流程").withTag("Oncreate函数：", "子追踪").start();
                try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                    // 获取id接口
                    id= UserOperate.getQqId(qqId, context);
                } catch (Exception e) {
                    TracingHelper.onError(e, span);
                    throw e;
                } finally {
                    span.finish();
                }
                if(id!=-1)
                {
                    /**
                     * 设置全局ID
                     */
                    UserInfo.setID(id);
                    /**
                     * 接收返回结果
                     */
                    String imageUrl="";
                    Span span3 = tracer.buildSpan("通过id获取图片url").withTag("Oncreate函数：", "子追踪").start();
                    try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                        // 调用获取头像url接口
                        imageUrl=UserInfoOperate.getImage(id,context);
                    } catch (Exception e) {
                        TracingHelper.onError(e, span);
                        throw e;
                    } finally {
                        span.finish();
                    }
                    /**
                     * 得到url
                     */
                    if(!imageUrl.equals(""))
                    {
                        /**
                         * 获取本地存储图片路径
                         */
                        SharedPreferences imagePath=context.getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
                        String imageAbsolutePath=imagePath.getString("imagepath","");
                        String nativeName= GetFileNameUtil.GetFileName(imageAbsolutePath);
                        /**
                         * 如果本地和用户表中存储的文件名一致，则直接用本地的
                         */
                        if(imageUrl.equals(nativeName))
                        {
                            /**
                             * 设置全局URL
                             */
                            UserInfo.setUrl(imageAbsolutePath);
                        }
                        /**
                         * 否则下载oss的文件到本地，并修改本地存储的图片路径
                         */
                        else
                        {
                            /**
                             * 下载
                             */
                            boolean IsDownLoad=false;
                            Span span4 = tracer.buildSpan("缓存头像流程").withTag("onCreaate函数：", "子追踪").start();
                            try (Scope ignored = tracer.scopeManager().activate(span,true)) {
                                // 调用下载接口
                                IsDownLoad=OssOperate.DownLoad(imageUrl, getSandBoxPath(context)+imageUrl);
                            } catch (Exception e) {
                                TracingHelper.onError(e, span);
                                throw e;
                            } finally {
                                span.finish();
                            }
                            if(IsDownLoad)
                                UserInfo.setUrl(getSandBoxPath(context)+imageUrl);
                            else
                            {
                                Log.e(TAG,"从oss缓存头像URL失败");
                            }
                            /**
                             * 设置全局URL，因为是异步下载图片，所以可能会直接忽略结果返回失败，因此直接默认会缓存成功
                             */
                            UserInfo.setUrl(getSandBoxPath(context)+imageUrl);
                        }
                    }
                    /**
                     * 未得到url
                     */
                    else{
                        Log.e(TAG,"Id获取头像URL失败");
                    }
                }

                else
                {
                    System.out.println("qqId获取用户ID失败");
                }
            }
        }).start();
        return true;
    }
    /**
     * 创建自定义输出目录
     *
     * @return 路径
     */
    private static String getSandBoxPath(Context context) {
        File externalFilesDir = context.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

}
