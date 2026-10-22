package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生选课/报名记录实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_enrollment")
public class AsEnrollmentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 服务ID
     */
    private Long serviceId;

    /**
     * 课程班ID
     */
    private Long courseClassId;
    /**
     * 课次id,t_as_course_session.id，区分唯一
     */
    private Long sessionId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生学号，选课流程展示冗余
     */
    private String studentNo;

    /**
     * 报名来源：1家长公开报名 2教师邀请 3行政班安排
     */
    private Integer enrollSource;

    /**
     * 报名状态：0待确认 1已报名 2已取消 3已分配 4未分配
     */
    private Integer enrollStatus;

    /**
     * 来源邀请批次ID
     */
    private Long inviteId;

    /**
     * 是否需要家长确认：0否 1是
     */
    private Integer parentConfirmRequired;

    /**
     * 家长确认账号
     */
    private Long guardianConfirmUserId;

    /**
     * 家长确认时间
     */
    private Date guardianConfirmTime;

    /**
     * 最近操作来源：1家长 2教师 3管理员 4系统
     */
    private Integer operatorType;

    /**
     * 最近操作用户ID
     */
    private Long operatorUserId;

    /**
     * 最近操作人展示名称
     */
    private String operatorName;

    /**
     * 报名/分配生效时间
     */
    private Date enrollTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 报名/分配备注
     */
    private String remark;
}
