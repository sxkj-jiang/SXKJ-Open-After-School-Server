package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程排课冲突类型枚举。
 */
@Getter
@AllArgsConstructor
public enum CourseScheduleConflictTypeEnum {

    /**
     * 同一课程班在同一日期存在重复上课时间。
     */
    SAME_COURSE_DUPLICATE(
            "SAME_COURSE_DUPLICATE",
            "课程「{courseName}」在 {date} {timeRange} 存在重复排课"
    ),

    /**
     * 本次保存的多门课程之间，同一授课教师在同一时段存在时间重叠。
     */
    TEACHER_TIME_INTERNAL(
            "TEACHER_TIME_INTERNAL",
            "课程「{courseName}」与「{conflictCourseName}」的授课教师在同一时段（{date} {timeRange}）存在时间冲突"
    ),

    /**
     * 本次保存的课程与数据库已有课程之间，同一授课教师在同一时段存在时间重叠。
     */
    TEACHER_TIME_WITH_EXISTING(
            "TEACHER_TIME_WITH_EXISTING",
            "课程「{courseName}」的授课教师与已有课程「{conflictCourseName}」在 {date} {timeRange} 存在时间冲突"
    ),

    /**
     * 本次保存的多门课程之间，同一上课地点在同一时段存在占用重叠（仅当课程设置了上课地点时校验）。
     */
    PLACE_TIME_INTERNAL(
            "PLACE_TIME_INTERNAL",
            "课程「{courseName}」与「{conflictCourseName}」在地点「{placeName}」的 {date} {timeRange} 存在占用冲突"
    ),

    /**
     * 本次保存的课程与数据库已有课程之间，同一上课地点在同一时段存在占用重叠（仅当课程设置了上课地点时校验）。
     */
    PLACE_TIME_WITH_EXISTING(
            "PLACE_TIME_WITH_EXISTING",
            "课程「{courseName}」与已有课程「{conflictCourseName}」在地点「{placeName}」的 {date} {timeRange} 存在占用冲突"
    ),

    /**
     * 本次保存的多门课程之间，适用年级同一班级在同一时段存在重复安排。
     */
    CLASS_SCOPE_TIME_INTERNAL(
            "CLASS_SCOPE_TIME_INTERNAL",
            "课程「{courseName}」与「{conflictCourseName}」的适用班级在 {date} {timeRange} 存在时间冲突"
    ),

    /**
     * 本次保存的课程与数据库已有课程之间，适用年级同一班级在同一时段存在重复安排。
     */
    CLASS_SCOPE_TIME_WITH_EXISTING(
            "CLASS_SCOPE_TIME_WITH_EXISTING",
            "课程「{courseName}」与已有课程「{conflictCourseName}」的适用班级在 {date} {timeRange} 存在时间冲突"
    );

    private final String code;
    private final String messageTemplate;

    /**
     * 根据占位符渲染提示文案。
     */
    public String formatMessage(String courseName, String conflictCourseName, String placeName,
                                String date, String timeRange) {
        return messageTemplate
                .replace("{courseName}", safe(courseName))
                .replace("{conflictCourseName}", safe(conflictCourseName))
                .replace("{placeName}", safe(placeName))
                .replace("{date}", safe(date))
                .replace("{timeRange}", safe(timeRange));
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
