package com.pfm.category.redis;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {
    @Resource(name="redisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name="redisTemplate")
    private ListOperations<String,String> listOps;

    public void addString (String key, String value){
        listOps.leftPush(key,value);
    }

    public void addStringWithCategory(String key, String value, Long categoryId) {
        String jsonValue = String.format("{\"value\":\"%s\", \"categoryId\":%d}", value, categoryId);
        redisTemplate.opsForList().leftPush(key, jsonValue);
    }

    public List<String> get(String key) {
        return listOps.getOperations().opsForList().range(key, 0, -1);
    }

}
