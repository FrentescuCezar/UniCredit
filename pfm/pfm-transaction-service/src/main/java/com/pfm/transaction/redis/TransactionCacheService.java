package com.pfm.transaction.redis;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.pfm.transaction.service.dto.TransactionDTO;

@Service
@RequiredArgsConstructor
public class TransactionCacheService {

    private static final String CACHE_PREFIX = "transaction:";

    @Resource(name = "redisTemplate")
    private final RedisTemplate<String, TransactionDTO> redisTemplate;

    public void cacheTransaction(Long transactionId, TransactionDTO transaction) {
        String key = CACHE_PREFIX + transactionId;
        redisTemplate.opsForValue().set(key, transaction, 10, TimeUnit.MINUTES);
    }

    public TransactionDTO getCachedTransaction(Long transactionId) {
        String key = CACHE_PREFIX + transactionId;
        return redisTemplate.opsForValue().get(key);
    }

    public void evictTransactionCache(Long transactionId) {
        String key = CACHE_PREFIX + transactionId;
        redisTemplate.delete(key);
    }
}
