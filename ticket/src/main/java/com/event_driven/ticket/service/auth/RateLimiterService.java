package com.event_driven.ticket.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    public boolean isAllowed(String key, int limit, int seconds){

        Long current = redisTemplate.opsForValue().increment(key);

        if(current != null && current == 1){
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }

        return current != null && current <= limit;
    }

    public void reset(String key) {
        redisTemplate.delete(key);
    }

}
