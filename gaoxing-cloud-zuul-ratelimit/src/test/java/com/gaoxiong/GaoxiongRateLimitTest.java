package com.gaoxiong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GaoxiongRateLimitTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        Collection<RedisServer> masters = redisTemplate.getConnectionFactory().getSentinelConnection().masters();
        for (RedisServer master : masters) {
            System.out.println("master = " + master);
        }
        List clientList = redisTemplate.getClientList();
        for (Object o : clientList) {
            System.out.println("o = " + o);
        }

        redisTemplate.opsForValue().set("test","gaoxiong" );
    }

    @Test
    public void test2(){
        Object test = redisTemplate.opsForValue().get("test");
        System.out.println("test = " + test);
    }
}