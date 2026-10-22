package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务学生提醒去重记录实体。
 */
@Data
@TableName("t_as_service_student_notice")
public class AsServiceStudentNoticeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校。
     */
    private Long schoolId;

    /**
     * 课后服务编号。
     */
    private Long serviceId;

    /**
     * 学生稳定身份编号。
     */
    private Long studentIdentityId;

    /**
     * 实际接收提醒的家长用户编号集合。
     */
    private String guardianUserIds;

    /**
     * 提醒类型：1报名开放期补发提醒。
     */
    private Integer noticeType;
}
