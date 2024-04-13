package com.pfm.category.redis;

import com.pfm.category.repository.KeywordRepository;
import com.pfm.category.repository.model.KeywordEntity;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Resource(name="redisTemplate")
    private ListOperations<String,String> listOps;

    public void addString (String key, String value){
        listOps.leftPush(key,value);
    }

    public List<String> get(String key) {
        return listOps.getOperations().opsForList().range(key, 0, -1);
    }

}
