package com.gaoxiong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GaoxiongRateLimitTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        redisTemplate.opsForValue().set("test","gaoxiong" );
    }

    @Test
    public void test2(){
        Object test = redisTemplate.opsForValue().get("test");
        System.out.println("test = " + test);
    }
}