package com.sxkj.edu.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxkj.edu.domain.sys.entity.UserLoginLogEntity;
import com.sxkj.edu.domain.sys.entity.UserOperateLogEntity;
import com.sxkj.edu.module.business.dao.UserLoginLogDao;
import com.sxkj.edu.module.business.dao.UserOperateLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 *@Description : 系统日志自动清理定时任务
 * 每月1号凌晨2:00执行，自动清理超过360天的登录日志和操作日志
 * 采用逻辑删除（isValid置0）+ 分批处理策略，避免大数据量下长时间锁表
 * 单次执行设置处理上限，超出部分等下月调度继续清理
 * 通过 update_time 标记已处理记录，确保中断后不会重复处理
 *@Author : auto
 *@Date : 2026-9-16
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Component
@Slf4j
public class SysLogJob {

    /** 保留天数 */
    private static final int RETAIN_DAYS = 360;
    /** 每批处理条数 */
    private static final int BATCH_SIZE = 5000;
    /** 单次执行每张表最大处理条数（防止数据积压时单次执行过久） */
    private static final int MAX_PER_TABLE = 500000;

    @Resource
    private UserLoginLogDao userLoginLogDao;

    @Resource
    private UserOperateLogDao userOperateLogDao;

    /**
     * 每月1号凌晨2:00执行，清理超过360天的系统日志
     */
//    @Scheduled(cron = "0 0 2 1 * ?")
    public void cleanExpiredLogs() {
        log.info("【系统日志清理定时任务】开始执行");
        try {
            // 计算截止时间：当前时间往前推360天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -RETAIN_DAYS);
            Date cutoffDate = calendar.getTime();

            // 分批逻辑删除登录日志
            int loginTotal = batchLogicalDeleteLoginLog(cutoffDate);
            log.info("【系统日志清理定时任务】逻辑删除登录日志共 {} 条（截止时间：{}）", loginTotal, cutoffDate);

            // 分批逻辑删除操作日志
            int operateTotal = batchLogicalDeleteOperateLog(cutoffDate);
            log.info("【系统日志清理定时任务】逻辑删除操作日志共 {} 条（截止时间：{}）", operateTotal, cutoffDate);

            log.info("【系统日志清理定时任务】执行完成，共处理 {} 条记录", loginTotal + operateTotal);
        } catch (Exception e) {
            log.error("【系统日志清理定时任务】执行异常", e);
        }
    }

    /**
     * 分批逻辑删除过期登录日志，每次处理 BATCH_SIZE 条
     * 单表最多处理 MAX_PER_TABLE 条，超出部分等下次调度继续
     * 以 update_time < cutoffDate 为筛选条件，处理时刷新 update_time，
     * 确保中断后已处理记录不会被重复处理
     */
    private int batchLogicalDeleteLoginLog(Date cutoffDate) {
        int totalUpdated = 0;
        int updated;
        do {
            Date now = new Date();
            updated = userLoginLogDao.update(null,
                    new LambdaUpdateWrapper<UserLoginLogEntity>()
                            .set(UserLoginLogEntity::getIsValid, 0)
                            .set(UserLoginLogEntity::getUpdateTime, now)
                            .lt(UserLoginLogEntity::getUpdateTime, cutoffDate)
                            .eq(UserLoginLogEntity::getIsValid, 1)
                            .last("LIMIT " + BATCH_SIZE));
            totalUpdated += updated;
            if (updated > 0) {
                log.info("【系统日志清理定时任务】登录日志本批逻辑删除 {} 条，累计 {} 条", updated, totalUpdated);
            }
            if (totalUpdated >= MAX_PER_TABLE) {
                log.warn("【系统日志清理定时任务】登录日志已达单次上限 {} 条，剩余数据留待下月清理", MAX_PER_TABLE);
                break;
            }
        } while (updated >= BATCH_SIZE);
        return totalUpdated;
    }

    /**
     * 分批逻辑删除过期操作日志，每次处理 BATCH_SIZE 条
     * 单表最多处理 MAX_PER_TABLE 条，超出部分等下次调度继续
     * 以 update_time < cutoffDate 为筛选条件，处理时刷新 update_time，
     * 确保中断后已处理记录不会被重复处理
     */
    private int batchLogicalDeleteOperateLog(Date cutoffDate) {
        int totalUpdated = 0;
        int updated;
        do {
            Date now = new Date();
            updated = userOperateLogDao.update(null,
                    new LambdaUpdateWrapper<UserOperateLogEntity>()
                            .set(UserOperateLogEntity::getIsValid, 0)
                            .set(UserOperateLogEntity::getUpdateTime, now)
                            .lt(UserOperateLogEntity::getUpdateTime, cutoffDate)
                            .eq(UserOperateLogEntity::getIsValid, 1)
                            .last("LIMIT " + BATCH_SIZE));
            totalUpdated += updated;
            if (updated > 0) {
                log.info("【系统日志清理定时任务】操作日志本批逻辑删除 {} 条，累计 {} 条", updated, totalUpdated);
            }
            if (totalUpdated >= MAX_PER_TABLE) {
                log.warn("【系统日志清理定时任务】操作日志已达单次上限 {} 条，剩余数据留待下月清理", MAX_PER_TABLE);
                break;
            }
        } while (updated >= BATCH_SIZE);
        return totalUpdated;
    }
}
