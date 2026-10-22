package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclCalendarEventEntity;
import com.sxkj.edu.module.afterschool.mapper.EduclCalendarEventMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 学期停课日期读取支持。
 *
 * <p>只读取当前学校、当前学期、开课周期内维护的有效校历事件，不修改校历和课次数据。</p>
 */
@Component
public class TermSuspensionDateSupport {

    private static final ZoneId COURSE_ZONE_ID = ZoneId.systemDefault();

    @Resource
    private EduclCalendarEventMapper educlCalendarEventMapper;

    /**
     * 查询课后服务开课周期内的全部停课日期。
     *
     * <p>校历事件的开始和结束日期均包含在停课范围内；结束日期为空时按单日处理。</p>
     */
    public Set<LocalDate> listSuspensionDates(AsServiceEntity service) {
        if (service == null) {
            return Collections.emptySet();
        }
        return listSuspensionDates(service.getSchoolId(), service.getTermId(),
                service.getCourseStartDate(), service.getCourseEndDate());
    }

    /**
     * 按学校、学期和日期区间读取停课日期，一次查询后在内存中展开日期范围。
     */
    public Set<LocalDate> listSuspensionDates(Long schoolId, Long termId, Date rangeStart, Date rangeEnd) {
        LocalDate start = toLocalDate(rangeStart);
        LocalDate end = toLocalDate(rangeEnd);
        if (schoolId == null || termId == null || start == null || end == null || start.isAfter(end)) {
            return Collections.emptySet();
        }

        LambdaQueryWrapper<EduclCalendarEventEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclCalendarEventEntity::getSchoolId, schoolId)
                .eq(EduclCalendarEventEntity::getTermId, termId)
                .eq(EduclCalendarEventEntity::getIsValid, 1)
                .le(EduclCalendarEventEntity::getEventDateStart, rangeEnd)
                .and(condition -> condition.ge(EduclCalendarEventEntity::getEventDateEnd, rangeStart)
                        .or(noneEnd -> noneEnd.isNull(EduclCalendarEventEntity::getEventDateEnd)
                                .ge(EduclCalendarEventEntity::getEventDateStart, rangeStart)))
                .orderByAsc(EduclCalendarEventEntity::getEventDateStart);
        List<EduclCalendarEventEntity> events = educlCalendarEventMapper.selectList(wrapper);
        if (events == null || events.isEmpty()) {
            return Collections.emptySet();
        }

        Set<LocalDate> result = new LinkedHashSet<>();
        for (EduclCalendarEventEntity event : events) {
            appendEventDates(result, event, start, end);
        }
        return result.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(result);
    }

    private void appendEventDates(Set<LocalDate> result, EduclCalendarEventEntity event,
                                  LocalDate rangeStart, LocalDate rangeEnd) {
        if (event == null || event.getEventDateStart() == null) {
            return;
        }
        LocalDate eventStart = toLocalDate(event.getEventDateStart());
        LocalDate eventEnd = event.getEventDateEnd() == null
                ? eventStart : toLocalDate(event.getEventDateEnd());
        if (eventStart == null || eventEnd == null || eventStart.isAfter(eventEnd)) {
            return;
        }
        LocalDate cursor = eventStart.isBefore(rangeStart) ? rangeStart : eventStart;
        LocalDate upperBound = eventEnd.isAfter(rangeEnd) ? rangeEnd : eventEnd;
        while (!cursor.isAfter(upperBound)) {
            result.add(cursor);
            cursor = cursor.plusDays(1);
        }
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(COURSE_ZONE_ID).toLocalDate();
    }
}
