package com.example.cugerhuo.login.utils;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 * @author 施立豪
 */
public class ExecutorManager {
    private static ExecutorService threadExecutor;

    public static void run(Runnable var0) {
        threadExecutor.execute(var0);
    }

    static {
        threadExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
