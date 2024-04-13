package com.pfm.category.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis")
public class RedisController {

    private final RedisService redisService;

    @PostMapping("/add")
    public ResponseEntity<String> addStringToRedis(@RequestParam String key, @RequestParam String value) {
        redisService.addString(key, value);
        return ResponseEntity.ok("Added successfully");
    }

    @GetMapping("/get")
    public ResponseEntity<List<String>> getStringFromRedis(@RequestParam String key) {
        List<String> values = redisService.get(key);
        if (values == null || values.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(values);
    }
}