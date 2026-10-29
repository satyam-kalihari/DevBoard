package com.satyam.DevBoard.cache;

import com.satyam.DevBoard.dto.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskListCache {

    private static final Duration TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate redis;
    private final JsonMapper jsonMapper;

    @Value("${devboard.cache.enabled:true}")
    private boolean enabled;

    private String key(UUID projectId) {
        return "tasks:project:" + projectId;
    }

    public Optional<List<TaskResponse>> get(UUID projectId) {
        if (!enabled) return Optional.empty();
        try {
            String json = redis.opsForValue().get(key(projectId));
            if (json == null) return Optional.empty();
            return Optional.of(jsonMapper.readValue(json, new TypeReference<List<TaskResponse>>() {}));
        } catch (Exception e) {
            log.warn("Cache read failed for project {}, using the database", projectId, e);
            return Optional.empty();
        }
    }

    public void put(UUID projectId, List<TaskResponse> tasks) {
        if (!enabled) return;
        try {
            redis.opsForValue().set(key(projectId), jsonMapper.writeValueAsString(tasks), TTL);
        } catch (Exception e) {
            log.warn("Cache write failed for project {}", projectId, e);
        }
    }

    public void evict(UUID projectId) {
        if (!enabled) return;
        try {
            redis.delete(key(projectId));
        } catch (Exception e) {
            log.warn("Cache evict failed for project {}", projectId, e);
        }
    }
}