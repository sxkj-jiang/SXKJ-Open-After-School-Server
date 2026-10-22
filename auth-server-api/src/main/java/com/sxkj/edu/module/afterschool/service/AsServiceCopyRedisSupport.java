package com.sxkj.edu.module.afterschool.service;

import com.alibaba.fastjson.JSON;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.redis.RedisCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 课后服务复制缓存保障组件。
 * 使用分布式锁串行处理同一请求号，并缓存已经完成的复制结果。
 */
@Component
@Slf4j
public class AsServiceCopyRedisSupport {

    private static final String LOCK_SUFFIX = ":lock";
    private static final String RESULT_SUFFIX = ":result";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisCache redisCache;

    /**
     * 生成当前学校内唯一的复制请求缓存键。
     */
    public String buildRequestKey(Long schoolId, String requestNo) {
        String requestDigest = DigestUtils.sha256Hex(requestNo == null ? "" : requestNo.trim());
        return CoreConstant.AS_SERVICE_COPY_REDIS_PREFIX + schoolId + ":" + requestDigest;
    }

    /**
     * 尝试获取复制请求分布式锁。
     */
    public boolean tryLock(String requestKey) {
        try {
            RLock lock = redissonClient.getLock(requestKey + LOCK_SUFFIX);
            return lock.tryLock(CoreConstant.AS_SERVICE_COPY_LOCK_WAIT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "获取复制请求锁被中断，请稍后重试");
        } catch (Exception exception) {
            throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "复制请求缓存暂不可用，请稍后重试");
        }
    }

    /**
     * 释放当前线程持有的复制请求分布式锁。
     */
    public void unlock(String requestKey) {
        try {
            RLock lock = redissonClient.getLock(requestKey + LOCK_SUFFIX);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception exception) {
            log.error("释放课后服务复制请求锁失败，缓存键={}", requestKey, exception);
        }
    }

    /**
     * 获取已经完成的复制结果。
     */
    public CopyRecord getResult(String requestKey) {
        try {
            Object value = redisCache.getObjectValue(requestKey + RESULT_SUFFIX);
            if (value == null) {
                return null;
            }
            return JSON.parseObject(String.valueOf(value), CopyRecord.class);
        } catch (Exception exception) {
            throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "读取复制请求缓存失败，请稍后重试");
        }
    }

    /**
     * 缓存复制结果，缓存失败时由调用方回滚数据库事务。
     */
    public boolean saveResult(String requestKey, String requestHash, Long targetServiceId) {
        CopyRecord record = new CopyRecord();
        record.setRequestHash(requestHash);
        record.setTargetServiceId(targetServiceId);
        return redisCache.set(requestKey + RESULT_SUFFIX, JSON.toJSONString(record),
                CoreConstant.AS_SERVICE_COPY_RESULT_CACHE_SECONDS);
    }

    /**
     * 删除没有对应数据库记录的失效复制结果。
     */
    public void deleteResult(String requestKey) {
        try {
            redisCache.delete(requestKey + RESULT_SUFFIX);
        } catch (Exception exception) {
            log.error("删除失效的课后服务复制结果缓存失败，缓存键={}", requestKey, exception);
        }
    }

    /**
     * 缓存中保存的复制结果索引。
     */
    @Data
    public static class CopyRecord implements Serializable {

        private static final long serialVersionUID = 1L;

        /** 复制参数摘要 */
        private String requestHash;

        /** 复制后的课后服务编号 */
        private Long targetServiceId;
    }
}
