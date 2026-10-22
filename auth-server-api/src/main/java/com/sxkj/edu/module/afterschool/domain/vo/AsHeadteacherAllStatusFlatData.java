package com.sxkj.edu.module.afterschool.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 班主任全部Tab多状态扁平查询结果。
 * <p>由 MyBatis XML 多表 JOIN 一次查出报名记录及关联的课程班、类别、学期、年级、班级字段，
 * Service 层直接从扁平结果构建上下文 Map，避免逐表批量查询产生多次数据库请求。</p>
 *
 * @author system
 * @date 2026-09-16
 */
@Data
public class AsHeadteacherAllStatusFlatData implements Serializable {

    private static final long serialVersionUID = 1L;

    /* ---- 报名记录字段 (t_as_enrollment) ---- */

    private Long enrollmentId;

    private Long schoolId;

    private Long serviceId;

    private Long studentId;

    private String studentNo;

    private Long courseClassId;

    private Long sessionId;

    private Integer enrollStatus;

    private Integer enrollSource;

    private Date enrollTime;

    /* ---- 课后服务字段 (t_as_service) ---- */

    private Long serviceTermId;

    private String serviceName;

    /* ---- 课程班字段 (t_as_course_class) ---- */

    private Long categoryId;

    private String courseClassName;

    /* ---- 课程类别字段 (t_as_course_category) ---- */

    private String categoryName;

    /* ---- 学期字段 (t_educl_term) ---- */

    private String termName;

    /* ---- 年级字段 (t_educl_grade) ---- */

    private String gradeName;

    /* ---- 班级字段 (t_educl_class) ---- */

    private String className;
}
