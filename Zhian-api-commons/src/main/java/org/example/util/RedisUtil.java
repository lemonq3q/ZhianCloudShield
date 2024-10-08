package org.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  Redis 工具类
 * </p>
 *
 * @author Ya Shi
 * @since 2024/3/12 14:02
 */
@Component
public class RedisUtil {

    //批量插入k-v键值对
    public static void multiSetValue(RedisTemplate redisTemplate,Map<String,String> map,long ttl){
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) {
                map.forEach((k,v)->{
                    connection.set(k.getBytes(),v.getBytes());
                    connection.expire(k.getBytes(),ttl);
                });
                return null;
            }
        });
    }

    public static void multiSetValue(RedisTemplate redisTemplate,Map<String,String> map){
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) {
                map.forEach((k,v)->{
                    connection.set(k.getBytes(),v.getBytes());
                });
                return null;
            }
        });
    }
}

