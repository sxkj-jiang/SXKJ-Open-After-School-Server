package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生课后去向分配实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_student_destination")
public class AsStudentDestinationEntity extends BaseEntity implements Serializable {

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
     * 学生ID
     */
    private Long studentId;

    /**
     * 课次id,t_as_course_session.id，区分唯一
     */
    private Long sessionId;

    /**
     * 学生学号，去向/选课流程展示冗余
     */
    private String studentNo;

    /**
     * 去向类型：1课程班 2回家 3培训机构 4留班 5行政课 6其他
     */
    private Integer destinationType;

    /**
     * 课程班ID
     */
    private Long courseClassId;

    /**
     * 去向名称
     */
    private String destinationName;

    /**
     * 分配人用户ID
     */
    private Long assignUserId;

    /**
     * 分配时间
     */
    private Date assignTime;
}
