package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生选课操作日志实体
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@TableName("t_as_enrollment_log")
public class AsEnrollmentLogEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 当前选课记录ID
     */
    private Long enrollmentId;

    /**
     * 服务ID
     */
    private Long serviceId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 变更前课程班ID
     */
    private Long beforeCourseClassId;

    /**
     * 变更后课程班ID
     */
    private Long afterCourseClassId;

    /**
     * 变更前报名状态
     */
    private Integer beforeEnrollStatus;

    /**
     * 变更后报名状态
     */
    private Integer afterEnrollStatus;

    /**
     * 动作类型：1初始化 2公开报名 3邀请报名 4邀请确认 5邀请拒绝 6行政班安排 7手动分配 8改课 9取消
     */
    private Integer actionType;

    /**
     * 操作来源：1家长 2教师 3管理员 4系统
     */
    private Integer operatorType;

    /**
     * 操作用户ID
     */
    private Long operatorUserId;

    /**
     * 操作人展示名称
     */
    private String operatorName;

    /**
     * 备注
     */
    private String remark;
}
