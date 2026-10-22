package com.sxkj.edu.module.afterschool.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 选课状态统计 JOIN 查询结果（单次 SQL 关联报名记录、学生、课后服务、课程班）。
 *
 * @author system
 * @date 2026-08-10
 */
@Data
public class AsEnrollmentSelectionCountData implements Serializable {

    private static final long serialVersionUID = 1L;

    /* ---- 报名记录字段 ---- */

    private Long enrollmentId;

    private Long schoolId;

    private Long serviceId;

    private Long courseClassId;

    private Long sessionId;

    private Long studentId;

    private String studentNo;

    private Integer enrollSource;

    private Integer enrollStatus;

    private Long inviteId;

    private Date enrollTime;

    /* ---- 学生字段 ---- */

    private String studentName;

    private Long gradeId;

    private Long classId;

    private Integer registerStatus;

    private Long studentTermId;

    /* ---- 课后服务字段 ---- */

    private Long serviceTermId;

    /* ---- 课程班字段 ---- */

    private Integer enrollType;
}
