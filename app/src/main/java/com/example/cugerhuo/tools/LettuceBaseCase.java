package com.example.cugerhuo.tools;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * 使用Lettuce操作redis
 * @author 施立豪 施立豪
 * @time 2023/4/10
 */
public class LettuceBaseCase {
    private static RedisClient client;
    private StatefulRedisConnection<String, String> connection;
    public  LettuceBaseCase() {
        RedisURI redisUri = RedisURI.builder()
                //配置连接
                .withHost("123.249.120.9").withPort(8083).withPassword("CUGerhuo333")
                //设置超时时间10s
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        client = RedisClient.create(redisUri);
        connection = client.connect();
    }
    public void close() {
        connection.close();
        client.shutdown();
    }
    /**
     * 同步操作连接
     * @author 施立豪
     * @time 2023/4/10
     */
    public RedisCommands<String, String> getSyncConnection() {
        return connection.sync();
    }
    /**
     * 异步操作连接
     * @author 施立豪
     * @time 2023/4/10
     */
    public RedisAsyncCommands<String, String> getAsyncConnection() {
        return connection.async();
    }
}