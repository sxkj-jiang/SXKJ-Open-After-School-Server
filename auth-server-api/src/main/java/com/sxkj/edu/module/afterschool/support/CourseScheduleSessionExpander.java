package com.sxkj.edu.module.afterschool.support;

import com.alibaba.fastjson.JSON;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseScheduleRuleAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseScheduleRuleEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 将课程班排课规则展开为具体上课时段的工具类。
 */
public final class CourseScheduleSessionExpander {

    private static final int RULE_TYPE_WEEKLY = 1;
    private static final ZoneId COURSE_ZONE_ID = ZoneId.systemDefault();

    private CourseScheduleSessionExpander() {
    }


    /**
     * 将排课规则展开为具体课次，并跳过当前学期维护的停课日期。
     */
    public static List<CourseScheduleSlot> expand(AsServiceEntity service,
                                                  AsCourseClassEntity course,
                                                  List<AsCourseScheduleRuleAO> rules,
                                                  boolean fromRequest,
                                                  Set<LocalDate> suspensionDates) {
        if (service == null || course == null || CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        List<CourseScheduleSlot> result = new ArrayList<>();
        for (AsCourseScheduleRuleAO rule : rules) {
            result.addAll(expandRule(service, course, rule, fromRequest, suspensionDates));
        }
        return result;
    }


    /**
     * 将数据库排课规则展开为具体课次，并跳过当前学期维护的停课日期。
     */
    public static List<CourseScheduleSlot> expandFromEntities(AsServiceEntity service,
                                                              AsCourseClassEntity course,
                                                              List<AsCourseScheduleRuleEntity> rules,
                                                              boolean fromRequest,
                                                              Set<LocalDate> suspensionDates) {
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        List<AsCourseScheduleRuleAO> ruleAos = new ArrayList<>();
        for (AsCourseScheduleRuleEntity rule : rules) {
            ruleAos.add(toRuleAO(rule));
        }
        return expand(service, course, ruleAos, fromRequest, suspensionDates);
    }

    public static boolean isWeeklyRule(AsCourseScheduleRuleAO rule) {
        return rule != null && ("weekly".equalsIgnoreCase(rule.getRuleType()) || "1".equals(rule.getRuleType()));
    }

    public static boolean isDateRule(AsCourseScheduleRuleAO rule) {
        return rule != null && ("date".equalsIgnoreCase(rule.getRuleType()) || "2".equals(rule.getRuleType()));
    }

    public static boolean sameDate(Date left, Date right) {
        return left != null && right != null && normalizeDate(left).equals(normalizeDate(right));
    }

    public static boolean timeOverlap(String start1, String end1, String start2, String end2) {
        Date s1 = parseTime(start1);
        Date e1 = parseTime(end1);
        Date s2 = parseTime(start2);
        Date e2 = parseTime(end2);
        if (s1 == null || e1 == null || s2 == null || e2 == null) {
            return false;
        }
        return s1.before(e2) && s2.before(e1);
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(normalizeDate(date));
    }

    public static String formatTimeRange(String startTime, String endTime) {
        return safe(startTime) + "-" + safe(endTime);
    }

    private static List<CourseScheduleSlot> expandRule(AsServiceEntity service,
                                                       AsCourseClassEntity course,
                                                       AsCourseScheduleRuleAO rule,
                                                       boolean fromRequest,
                                                       Set<LocalDate> suspensionDates) {
        List<CourseScheduleSlot> result = new ArrayList<>();
        if (isWeeklyRule(rule)) {
            Date cursor = normalizeDate(service.getCourseStartDate());
            Date end = normalizeDate(service.getCourseEndDate());
            if (cursor == null || end == null || CollectionUtils.isEmpty(rule.getWeekDays())) {
                return result;
            }
            while (!cursor.after(end)) {
                if (rule.getWeekDays().contains(getWeekDay(cursor))
                        && !isSuspensionDate(cursor, suspensionDates)) {
                    result.add(toSlot(course, rule, cursor, fromRequest));
                }
                cursor = addDays(cursor, 1);
            }
        } else if (isDateRule(rule) && rule.getSessionDate() != null
                && !isSuspensionDate(rule.getSessionDate(), suspensionDates)) {
            result.add(toSlot(course, rule, normalizeDate(rule.getSessionDate()), fromRequest));
        }
        return result;
    }

    /**
     * 判断指定课次日期是否命中已加载的停课日期集合。
     */
    public static boolean isSuspensionDate(Date date, Set<LocalDate> suspensionDates) {
        if (date == null || CollectionUtils.isEmpty(suspensionDates)) {
            return false;
        }
        LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(COURSE_ZONE_ID).toLocalDate();
        return suspensionDates.contains(localDate);
    }

    private static CourseScheduleSlot toSlot(AsCourseClassEntity course,
                                             AsCourseScheduleRuleAO rule,
                                             Date date,
                                             boolean fromRequest) {
        CourseScheduleSlot slot = new CourseScheduleSlot();
        slot.setCourseClassId(course.getId());
        slot.setCourseClassName(course.getCourseClassName());
        slot.setServiceId(course.getServiceId());
        slot.setSessionDate(date);
        slot.setStartTime(rule.getStartTime());
        slot.setEndTime(rule.getEndTime());
        slot.setTeacherId(rule.getTeacherId() == null ? course.getTeacherId() : rule.getTeacherId());
        slot.setPlaceName(StringUtils.hasText(rule.getPlaceId()) ? rule.getPlaceId() : course.getPlaceId());
        slot.setApplicableClassIds(course.getApplicableClassIds());
        slot.setApplicableGradeIds(course.getApplicableGradeIds());
        slot.setEnrollType(course.getEnrollType());
        slot.setFromRequest(fromRequest);
        return slot;
    }

    private static AsCourseScheduleRuleAO toRuleAO(AsCourseScheduleRuleEntity entity) {
        AsCourseScheduleRuleAO rule = new AsCourseScheduleRuleAO();
        rule.setRuleType(Objects.equals(entity.getRuleType(), RULE_TYPE_WEEKLY) ? "weekly" : "date");
        rule.setWeekDays(parseIntegerList(entity.getWeekDaysJson()));
        rule.setSessionDate(entity.getSessionDate());
        rule.setSchedulePeriodId(entity.getSchedulePeriodId());
        rule.setStartTime(formatTime(entity.getStartTime()));
        rule.setEndTime(formatTime(entity.getEndTime()));
        rule.setPlaceId(entity.getPlaceName());
        rule.setTeacherId(entity.getTeacherId());
        return rule;
    }

    private static List<Integer> parseIntegerList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(json, Integer.class);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private static Date normalizeDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == Calendar.SUNDAY ? 7 : day - 1;
    }

    private static Date parseTime(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new SimpleDateFormat("HH:mm").parse(value.trim());
        } catch (ParseException ex) {
            return null;
        }
    }

    private static String formatTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("HH:mm").format(date);
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
