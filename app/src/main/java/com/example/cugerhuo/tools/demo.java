package com.example.cugerhuo.tools;

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
}
