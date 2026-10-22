package com.sxkj.edu.module.afterschool.enums;

/**
 * 后端内部容量策略，不作为接口入参；普通报名和分配始终执行严格限额。
 */
public enum CourseCapacityPolicy {
    /** 普通报名、邀请和分配，不允许超过课程或年级人数上限。 */
    ENFORCE_LIMIT,
    /** 学生调课申请及审核专用，允许满额调入但仍需真实占位。 */
    STUDENT_ADJUST
}
