package com.sxkj.edu.module.afterschool.service;

import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 家长公开报名分布式锁组件。
 * 同一学生报名同一课程班时串行处理，课程人数校准时额外串行保护课程班。
 */
@Component
@Slf4j
public class AsEnrollmentApplyRedisSupport {

    private static final String STUDENT_COURSE_LOCK_SUFFIX = "student_course:";
    private static final String CAPACITY_REPAIR_LOCK_SUFFIX = "capacity_repair:";

    @Resource
    private RedissonClient redissonClient;

    /**
     * 生成学生报名课程班锁缓存键。
     */
    public String buildStudentCourseLockKey(Long courseClassId, Long studentId) {
        return CoreConstant.AS_ENROLLMENT_APPLY_REDIS_PREFIX
                + STUDENT_COURSE_LOCK_SUFFIX + courseClassId + ":" + studentId;
    }

    /**
     * 生成课程班人数校准锁缓存键。
     */
    public String buildCapacityRepairLockKey(Long courseClassId) {
        return CoreConstant.AS_ENROLLMENT_APPLY_REDIS_PREFIX
                + CAPACITY_REPAIR_LOCK_SUFFIX + courseClassId;
    }

    /**
     * 尝试获取公开报名分布式锁。
     */
    public boolean tryLock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            return lock.tryLock(CoreConstant.AS_ENROLLMENT_APPLY_LOCK_WAIT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "获取报名锁被中断，请稍后重试");
        } catch (Exception exception) {
            throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "报名服务暂不可用，请稍后重试");
        }
    }

    /**
     * 释放当前线程持有的公开报名分布式锁。
     */
    public void unlock(String lockKey) {
        if (lockKey == null) {
            return;
        }
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception exception) {
            log.error("释放家长公开报名锁失败，缓存键={}", lockKey, exception);
        }
    }
}
