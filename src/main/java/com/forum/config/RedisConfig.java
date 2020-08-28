package com.forum.config;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //设置key的序列化方式
        redisTemplate.setKeySerializer(stringSerializer);

        //设置value的序列化方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        //设置hash的key的序列化方式
        redisTemplate.setHashKeySerializer(stringSerializer);

        //设置hash的value的序列化方式
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        //使设置生效
        redisTemplate.afterPropertiesSet();

        return redisTemplate;

    }

}
