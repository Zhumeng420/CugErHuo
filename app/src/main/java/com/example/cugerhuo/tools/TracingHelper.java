package com.example.cugerhuo.tools;

import io.opentracing.Span;
import io.opentracing.tag.Tags;

import java.util.HashMap;
import java.util.Map;

public class TracingHelper {
    public static void onError(Throwable throwable, Span span) {
        Tags.ERROR.set(span, Boolean.TRUE);
        if (throwable != null) {
            span.log(errorLogs(throwable));
        }
    }

    private static Map<String, Object> errorLogs(Throwable throwable) {
        Map<String, Object> errorLogs = new HashMap<>(2);
        errorLogs.put("event", Tags.ERROR.getKey());
        errorLogs.put("error.object", throwable);
        errorLogs.put("error.kind", throwable.getClass().getName());

        return errorLogs;
    }
}
