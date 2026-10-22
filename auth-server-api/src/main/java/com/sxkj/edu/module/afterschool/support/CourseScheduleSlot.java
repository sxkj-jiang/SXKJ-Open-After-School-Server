package com.sxkj.edu.module.afterschool.support;

import lombok.Data;

import java.util.Date;

/**
 * 排课规则展开后的单次上课时段，用于冲突检测。
 */
@Data
public class CourseScheduleSlot {

    private Long courseClassId;

    private String courseClassName;

    private Long serviceId;

    private Date sessionDate;

    private String startTime;

    private String endTime;

    private Long teacherId;

    private String placeName;

    private String applicableClassIds;

    private String applicableGradeIds;

    private Integer enrollType;

    /**
     * 是否来自本次保存请求中的课程（用于区分内部冲突与已有课程冲突）。
     */
    private boolean fromRequest;
}
