package com.sxkj.edu.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import com.sxkj.edu.module.afterschool.service.AsServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 课后服务状态定时任务
 * <p>每天晚上23:00执行，自动更新课后服务状态：
 * <ul>
 *   <li>规则1：状态为"报名中"(1)，当前时间在报名时间范围内，更新为"进行中"(2)</li>
 *   <li>规则2：状态为"进行中"(2)，当前时间超过结课日期，更新为"已结束"(3)</li>
 * </ul>
 * </p>
 */
@Component
@Slf4j
public class AsServiceStatusScheduleJob {


    @Resource
    private AsServiceService asServiceService;

    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;

    /**
     * 每天晚上23:00执行课后服务状态流转
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void updateServiceStatus() {
        log.info("【课后服务状态定时任务】开始执行");
        asServiceService.taskUpdateServiceMd();
    }

    /**
     * 每隔一小时执行课次状态更新任务
     * <p>更新逻辑：
     * <ul>
     *   <li>当前时间 < 课次开始时间(session_date+start_time) → session_status = 0 (未开始)</li>
     *   <li>课次开始时间 <= 当前时间 < 课次结束时间(session_date+end_time) → session_status = 1 (进行中)</li>
     *   <li>当前时间 >= 课次结束时间(session_date+end_time) → session_status = 2 (已结束)</li>
     * </ul>
     * </p>
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void updateSessionStatus() {
        log.info("【课次状态定时任务】开始执行");
        try {
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);

            // 查询所有有效且状态不为取消(3)的课次
            LambdaQueryWrapper<AsCourseSessionEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AsCourseSessionEntity::getIsValid, 1)
                    .ne(AsCourseSessionEntity::getSessionStatus, 3); // 排除已取消的课次

            List<AsCourseSessionEntity> sessionList = asCourseSessionMapper.selectList(queryWrapper);

            if (sessionList == null || sessionList.isEmpty()) {
                log.info("【课次状态定时任务】无需更新的课次");
                return;
            }

            log.info("【课次状态定时任务】共查询到 {} 条课次记录", sessionList.size());

            int updateCount = 0;
            for (AsCourseSessionEntity session : sessionList) {
                if (session.getSessionDate() == null || session.getStartTime() == null || session.getEndTime() == null) {
                    continue;
                }

                // 组装课次开始时间：session_date + start_time
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(session.getSessionDate());
                Calendar startTimeCal = Calendar.getInstance();
                startTimeCal.setTime(session.getStartTime());
                startCalendar.set(Calendar.HOUR_OF_DAY, startTimeCal.get(Calendar.HOUR_OF_DAY));
                startCalendar.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE));
                startCalendar.set(Calendar.SECOND, startTimeCal.get(Calendar.SECOND));
                Date sessionStartTime = startCalendar.getTime();

                // 组装课次结束时间：session_date + end_time
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(session.getSessionDate());
                Calendar endTimeCal = Calendar.getInstance();
                endTimeCal.setTime(session.getEndTime());
                endCalendar.set(Calendar.HOUR_OF_DAY, endTimeCal.get(Calendar.HOUR_OF_DAY));
                endCalendar.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE));
                endCalendar.set(Calendar.SECOND, endTimeCal.get(Calendar.SECOND));
                Date sessionEndTime = endCalendar.getTime();

                // 判断当前时间处于哪个阶段
                Integer newStatus = null;
                if (now.compareTo(sessionStartTime) >= 0 && now.before(sessionEndTime)) {
                    // 课次开始时间 <= 当前时间 < 课次结束时间 → 进行中
                    newStatus = 1;
                } else if (now.compareTo(sessionEndTime) >= 0) {
                    // 当前时间 >= 课次结束时间 → 已结束
                    newStatus = 2;
                }

                // 如果状态需要更新
                if (newStatus != null && !newStatus.equals(session.getSessionStatus())) {
                    LambdaUpdateWrapper<AsCourseSessionEntity> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(AsCourseSessionEntity::getId, session.getId())
                            .set(AsCourseSessionEntity::getSessionStatus, newStatus);
                    asCourseSessionMapper.update(null, updateWrapper);
                    updateCount++;
                    log.debug("【课次状态定时任务】课次ID {} 状态从 {} 更新为 {}", 
                            session.getId(), session.getSessionStatus(), newStatus);
                }
            }

            log.info("【课次状态定时任务】执行完成，共更新 {} 条课次记录", updateCount);
        } catch (Exception e) {
            log.error("【课次状态定时任务】执行异常", e);
        }
    }



}
