package com.gaoxiong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author gaoxiong
 * @ClassName RedisConfig
 * @Description TODO
 * @date 2019/8/29 16:36
 */
@Configuration
public class RedisConfig {

//    @Bean
    public RedisConnectionFactory redisConnectionFactory () {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.clusterNode("106.12.84.126", 6379);
        redisClusterConfiguration.clusterNode("106.12.84.126", 6380);
        redisClusterConfiguration.clusterNode("106.12.84.126", 6381);
        redisClusterConfiguration.setPassword("123456");
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration);

        return lettuceConnectionFactory;
    }
}
