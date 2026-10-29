package com.satyam.DevBoard.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLock {

    private final StringRedisTemplate redis;
    private final String owner = UUID.randomUUID().toString();   // identifies this app instance

    /** SET key owner NX EX ttl. True if this instance now holds the lock. */
    public boolean tryAcquire(String key, Duration ttl) {
        try {
            return Boolean.TRUE.equals(redis.opsForValue().setIfAbsent(key, owner, ttl));
        } catch (Exception e) {
            // The lock only saves duplicate work. The unique constraint prevents duplicate data,
            // so it is safe to carry on when Redis is down.
            log.warn("Redis unavailable, continuing without the lock", e);
            return true;
        }
    }
}