package com.sxkj.edu.module.afterschool.service;

import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsServicePublishAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsServicePublishResultVO;
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
 * 课后服务异步发布启动服务。
 *
 * <p>实现方式与行政班异步安排保持一致：同一课后服务在锁有效期内只启动一个发布任务，
 * 数据库发布中状态作为最终的重复提交保护。</p>
 */
@Service
@Slf4j
public class AsServicePublishAsyncService {

    private static final String LOCK_PREFIX = "edu:after_school:service_publish:";
    private static final long LOCK_SECONDS = 120L;
    private static final RedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get', KEYS[1]) == ARGV[1] then "
                    + "return redis.call('del', KEYS[1]) else return 0 end", Long.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final AsServiceService asServiceService;

    /** 构造课后服务异步发布启动服务。 */
    public AsServicePublishAsyncService(RedisTemplate<String, Object> redisTemplate,
                                        AsServiceService asServiceService) {
        this.redisTemplate = redisTemplate;
        this.asServiceService = asServiceService;
    }

    /**
     * 校验并标记发布中后启动异步发布任务。
     */
    public JsonResult<AsServicePublishResultVO> start(
            Long serviceId, AsServicePublishAO ao, Long operatorUserId) {
        if (serviceId == null) {
            return JsonResult.buildError("服务ID不能为空");
        }
        String lockKey = LOCK_PREFIX + serviceId;
        String lockToken = UUID.randomUUID().toString();
        Boolean locked;
        try {
            locked = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockToken, LOCK_SECONDS, TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("获取课后服务发布锁失败，serviceId={}", serviceId, exception);
            return JsonResult.buildError("发布任务启动失败，请稍后重试");
        }
        if (!Boolean.TRUE.equals(locked)) {
            return JsonResult.buildError("存在发布中的课后服务，请勿重复操作");
        }

        JsonResult<AsServicePublishResultVO> prepareResult;
        try {
            prepareResult = asServiceService.preparePublishService(serviceId, ao, operatorUserId);
            if (!Boolean.TRUE.equals(prepareResult.getSuccess())) {
                releaseLock(lockKey, lockToken);
                return prepareResult;
            }
            CompletableFuture<JsonResult<AsServicePublishResultVO>> future =
                    asServiceService.publishServiceAsync(serviceId, ao, operatorUserId);
            future.whenComplete((result, throwable) -> {
                try {
                    if (throwable != null || result == null || !Boolean.TRUE.equals(result.getSuccess())) {
                        asServiceService.restorePublishPreparingStatus(serviceId, operatorUserId);
                        if (throwable != null) {
                            log.error("课后服务异步发布失败，serviceId={}", serviceId, throwable);
                        } else {
                            log.error("课后服务异步发布失败，serviceId={}，原因={}",
                                    serviceId, result == null ? "无返回结果" : result.getMsg());
                        }
                    }
                } catch (Exception exception) {
                    log.error("恢复课后服务发布状态失败，serviceId={}", serviceId, exception);
                } finally {
                    releaseLock(lockKey, lockToken);
                }
            });
            return prepareResult;
        } catch (RuntimeException exception) {
            try {
                asServiceService.restorePublishPreparingStatus(serviceId, operatorUserId);
            } catch (Exception restoreException) {
                log.error("恢复课后服务发布状态失败，serviceId={}", serviceId, restoreException);
            }
            releaseLock(lockKey, lockToken);
            log.error("启动课后服务异步发布失败，serviceId={}", serviceId, exception);
            return JsonResult.buildError("发布任务启动失败，请稍后重试");
        }
    }

    /**
     * 仅释放当前任务持有的锁，防止误删锁过期后由新任务获取的锁。
     */
    private void releaseLock(String lockKey, String lockToken) {
        try {
            redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(lockKey), lockToken);
        } catch (Exception exception) {
            log.error("释放课后服务发布锁失败，lockKey={}", lockKey, exception);
        }
    }
}
