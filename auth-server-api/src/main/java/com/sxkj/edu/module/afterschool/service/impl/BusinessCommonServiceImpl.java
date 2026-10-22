package com.sxkj.edu.module.afterschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.DateHolidayEntity;
import com.sxkj.edu.module.afterschool.domain.vo.CalendarDayVO;
import com.sxkj.edu.module.afterschool.domain.vo.SchoolCalendarVO;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.service.BusinessCommonService;
import com.sxkj.edu.module.afterschool.service.DateHolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 通用业务 Service 实现类
 *
 * @author system
 * @date 2026-06-14
 */
@Service
@Slf4j
public class BusinessCommonServiceImpl implements BusinessCommonService {

    @Resource
    private AsServiceMapper asServiceMapper;

    @Resource
    private DateHolidayService dateHolidayService;

    private static final String[] WEEK_DAY_NAMES = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    @Override
    public SchoolCalendarVO querySchoolCalendar(Long schoolId, Integer weekNo) {
        // 1. 查询当前学校在开课时间范围内的课后服务
        AsServiceEntity service = findActiveService(schoolId);
        if (service == null) {
            log.info("【校历查询】schoolId={} 当前时间未命中任何课后服务", schoolId);
            return null;
        }

        Date courseStart = normalizeDate(service.getCourseStartDate());
        Date courseEnd = normalizeDate(service.getCourseEndDate());
        Date today = normalizeDate(new Date());

        // 2. 计算学期起始周一（courseStartDate 所在周的周一）
        Date semesterStartMonday = getMondayOfWeek(courseStart);

        // 3. 计算学期结束周日（courseEndDate 所在周的周日）
        Date semesterEndSunday = getSundayOfWeek(courseEnd);

        // 4. 计算总周数
        int totalWeeks = (int) (daysBetween(semesterStartMonday, semesterEndSunday) / 7) + 1;

        // 5. 计算当前周次
        int currentWeek = (int) (daysBetween(semesterStartMonday, getMondayOfWeek(today)) / 7) + 1;
        if (currentWeek < 1) {
            currentWeek = 1;
        }
        if (currentWeek > totalWeeks) {
            currentWeek = totalWeeks;
        }

        // 6. 确定目标周次（入参优先）
        int targetWeek = (weekNo != null && weekNo >= 1 && weekNo <= totalWeeks) ? weekNo : currentWeek;

        // 7. 计算目标周的周一和周日
        Date targetMonday = addDays(semesterStartMonday, (targetWeek - 1) * 7);
        Date targetSunday = addDays(targetMonday, 6);

        // 8. 查询目标周所在年份的节假日数据
        int year = getYear(targetMonday);
        List<DateHolidayEntity> holidays = dateHolidayService.getHolidaysByYear(year);

        // 9. 生成日历集合
        List<CalendarDayVO> calendarDays = buildCalendarDays(targetMonday, today, holidays);

        // 10. 组装返回
        SchoolCalendarVO vo = new SchoolCalendarVO();
        vo.setTotalWeeks(totalWeeks);
        vo.setCurrentWeek(targetWeek);
        vo.setCalendarDays(calendarDays);
        return vo;
    }

    /**
     * 查询当前时间在开课时间范围内的课后服务
     */
    private AsServiceEntity findActiveService(Long schoolId) {
        Date today = new Date();
        QueryWrapper<AsServiceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .eq("school_id", schoolId)
                .le("course_start_date", today)
                .ge("course_end_date", today)
                .orderByDesc("id")
                .last("LIMIT 1");
        List<AsServiceEntity> list = asServiceMapper.selectList(wrapper);
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    /**
     * 生成目标周的7天日历（周一至周日）
     */
    private List<CalendarDayVO> buildCalendarDays(Date monday, Date today, List<DateHolidayEntity> holidays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date todayNorm = normalizeDate(today);
        List<CalendarDayVO> days = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            Date day = addDays(monday, i);
            CalendarDayVO dayVO = new CalendarDayVO();
            dayVO.setDate(sdf.format(day));
            dayVO.setWeekDay(i + 1);
            dayVO.setWeekDayName(WEEK_DAY_NAMES[i + 1]);
            dayVO.setIsToday(day.equals(todayNorm));
            dayVO.setHolidayName(matchHolidayName(day, holidays));
            days.add(dayVO);
        }
        return days;
    }

    /**
     * 匹配节假日名称：判断指定日期是否落在某个节假日的 startDate ~ endDate 区间内
     */
    private String matchHolidayName(Date date, List<DateHolidayEntity> holidays) {
        if (holidays == null || holidays.isEmpty()) {
            return null;
        }
        Date dateNorm = normalizeDate(date);
        for (DateHolidayEntity holiday : holidays) {
            Date start = normalizeDate(holiday.getStartDate());
            Date end = normalizeDate(holiday.getEndDate());
            if (start != null && end != null
                    && !dateNorm.before(start) && !dateNorm.after(end)) {
                return holiday.getHolidayName();
            }
        }
        return null;
    }

    /**
     * 获取指定日期所在周的周一
     */
    private Date getMondayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定日期所在周的周日
     */
    private Date getSundayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     */
    private long daysBetween(Date start, Date end) {
        long diffMs = end.getTime() - start.getTime();
        return diffMs / (1000 * 60 * 60 * 24);
    }

    /**
     * 日期加减天数
     */
    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 归一化日期（去掉时分秒）
     */
    private Date normalizeDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取日期的年份
     */
    private int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}
