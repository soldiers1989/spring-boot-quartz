package com.pgy.ginko.quartz.config;

import com.pgy.ginko.quartz.model.test.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


/**
 * @author ginko
 * @description 测试RedisCache
 * @date 2018/8/22 17:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisCacheAutoConfigTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Test
    public void get() {
        // 测试线程安全
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0, 1000).forEach(i ->
                executorService.execute(() -> stringRedisTemplate.opsForValue().increment("kk", 1))
        );
        stringRedisTemplate.opsForValue().set("k1", "v1");
        /**
         * opsForValue： 对应 String（字符串）
         * opsForZSet： 对应 ZSet（有序集合）
         * opsForHash： 对应 Hash（哈希）
         * opsForList： 对应 List（列表）
         * opsForSet： 对应 Set（集合）
         * opsForGeo： 对应 GEO（地理位置）
         */
        final String k1 = stringRedisTemplate.opsForValue().get("k1");
        log.info("[字符缓存结果] - [{}]", k1);
        //  以下只演示整合，具体Redis命令可以参考官方文档，Spring Data Redis 只是改了个名字而已，Redis支持的命令它都支持
        String key = "ginko:user:1";
        redisCacheTemplate.opsForValue().set(key, new User(1L, "u1", "pa"));
        //  对应 String（字符串）
        final User user = (User) redisCacheTemplate.opsForValue().get(key);
        log.info("[对象缓存结果] - [{}]", user);
    }


}