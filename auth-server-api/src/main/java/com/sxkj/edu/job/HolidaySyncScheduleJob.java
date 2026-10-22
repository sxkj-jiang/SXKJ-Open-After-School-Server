package com.sxkj.edu.job;

import com.sxkj.edu.module.afterschool.service.DateHolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;


/**
 *@Description : 节假日数据同步定时任务
 * 每年1月1日凌晨1:00执行，自动从远程接口同步当年节假日数据到 t_date_holiday
 *@Author : yh
 *@Date : 2026-8-6 15:34
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Component
@Slf4j
public class HolidaySyncScheduleJob {

    @Resource
    private DateHolidayService dateHolidayService;

    /**
     * 每年1月1日凌晨1:00执行，同步当年节假日数据
     */
    @Scheduled(cron = "0 0 1 1 1 ?")
    public void syncHolidayData() {
        log.info("【节假日同步定时任务】开始执行");

        try {
            // 获取当前年份
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            // 同步当年数据
            int count = dateHolidayService.syncHolidayData(currentYear);

            log.info("【节假日同步定时任务】执行完成，同步 {} 年节假日数据 {} 条", currentYear, count);
        } catch (Exception e) {
            log.error("【节假日同步定时任务】执行异常", e);
        }
    }
}
