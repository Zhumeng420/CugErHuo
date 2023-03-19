package com.example.cugerhuo.tools;

import android.content.Intent;
import android.util.Log;

import com.example.cugerhuo.Activity.ErHuoActivity;
import com.example.cugerhuo.DataAccess.User.UserOperate;
import com.example.cugerhuo.FastLogin.login.OneKeyLoginActivity;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

/**
 * 链路追踪demo
 */
public class demo {
    void demooo(){
        Tracer tracer = GlobalTracer.get();
        // 创建spann
        Span span = tracer.buildSpan("登录流程").withTag("myTag", "spanFrist").start();
        try (Scope ignored = tracer.scopeManager().activate(span,true)) {
            tracer.activeSpan().setTag("getResultWithToken", "testTracing");
            // 业务逻辑写这里

        } catch (Exception e) {
            TracingHelper.onError(e, span);
            throw e;
        } finally {
            span.finish();
        }
    }
}
