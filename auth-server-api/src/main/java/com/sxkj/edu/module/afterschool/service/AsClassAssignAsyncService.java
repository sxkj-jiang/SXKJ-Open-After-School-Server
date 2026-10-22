package com.sxkj.edu.module.afterschool.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 行政班异步安排启动服务。
 * 同一课后服务在两分钟锁有效期内最多启动一个行政班安排任务。
 */
@Service
@Slf4j
public class AsClassAssignAsyncService {

    private static final String LOCK_PREFIX = "edu:after_school:class_assign:";
    private static final long LOCK_SECONDS = 120L;
    private static final RedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get', KEYS[1]) == ARGV[1] then "
                    + "return redis.call('del', KEYS[1]) else return 0 end", Long.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final AsServiceService asServiceService;

    public AsClassAssignAsyncService(RedisTemplate<String, Object> redisTemplate,
                                     AsServiceService asServiceService) {
        this.redisTemplate = redisTemplate;
        this.asServiceService = asServiceService;
    }

    /**
     * 获取 Redis 锁后启动行政班异步安排；已有任务执行时直接跳过。
     */
    public boolean start(Long serviceId) {
        if (serviceId == null) {
            return false;
        }
        String lockKey = LOCK_PREFIX + serviceId;
        String lockToken = UUID.randomUUID().toString();
        Boolean locked;
        try {
            locked = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockToken, LOCK_SECONDS, TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("获取行政班安排锁失败，本次不启动异步任务，serviceId={}", serviceId, exception);
            return false;
        }
        if (!Boolean.TRUE.equals(locked)) {
            log.warn("行政班安排任务正在执行，本次不再重复启动，serviceId={}", serviceId);
            return false;
        }
        try {
            CompletableFuture<Void> future = asServiceService.generateClassAssignEnrollmentsAsync(serviceId);
            future.whenComplete((ignored, throwable) -> {
                releaseLock(lockKey, lockToken);
                if (throwable != null) {
                    log.error("行政班异步安排执行失败，serviceId={}", serviceId, throwable);
                }
            });
            return true;
        } catch (RuntimeException exception) {
            releaseLock(lockKey, lockToken);
            throw exception;
        }
    }

    /**
     * 仅释放当前任务持有的锁，防止误删过期后被新任务重新获取的锁。
     */
    private void releaseLock(String lockKey, String lockToken) {
        try {
            redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey), lockToken);
        } catch (Exception exception) {
            log.error("释放行政班安排锁失败，lockKey={}", lockKey, exception);
        }
    }
}
