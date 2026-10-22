package com.sxkj.edu.module.afterschool.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 已选课组班分页查询扁平结果。
 * <p>由 MyBatis XML 多表 JOIN 一次查出列表行所需全部字段，
 * Service 层仅需做枚举翻译和少量后处理，避免多次批量查询。</p>
 *
 * @author system
 * @date 2026-08-10
 */
@Data
public class AsEnrollmentSelectedPageData implements Serializable {

    private static final long serialVersionUID = 1L;

    /* ---- 报名记录字段 (t_as_enrollment) ---- */

    private Long enrollmentId;

    private Long schoolId;

    private Long serviceId;

    private Long courseClassId;

    private Long sessionId;

    private Long studentId;

    private String studentNo;

    private Integer enrollSource;

    private Integer enrollStatus;

    private Integer operatorType;

    private Long operatorUserId;

    private String operatorName;

    private Date enrollTime;

    /* ---- 学生字段 (t_as_student) ---- */

    private String studentName;

    private Integer gender;

    private Long gradeId;

    private Long classId;

    private Integer registerStatus;

    private Long studentTermId;

    private Long studentIdentityId;

    /* ---- 课后服务字段 (t_as_service) ---- */

    private String serviceName;

    private Long serviceTermId;

    /* ---- 课程班字段 (t_as_course_class) ---- */

    private String courseClassName;

    private Long categoryId;

    private Integer enrollType;

    private Integer capacity;

    /* ---- 课程类别字段 (t_as_course_category) ---- */

    private String categoryName;

    /* ---- 学期字段 (t_educl_term) ---- */

    private String termName;

    /* ---- 年级字段 (t_educl_grade) ---- */

    private String gradeName;

    /* ---- 班级字段 (t_educl_class) ---- */

    private String className;

    /* ---- 已学课时 (子查询聚合) ---- */

    private Integer learnedSessions;
}
