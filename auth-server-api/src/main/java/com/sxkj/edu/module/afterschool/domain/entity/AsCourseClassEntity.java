package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务课程班实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_course_class")
public class AsCourseClassEntity extends BaseEntity implements Serializable {

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
     * 课程班编码
     */
    private String courseClassCode;

    /**
     * 课程班名称
     */
    private String courseClassName;

    /**
     * 课后类别ID
     */
    private Long categoryId;

    /**
     * 学习模块ID
     */
    private Long moduleId;

    /**
     * 报名方式：1公开报名 2邀请报名 3行政班安排
     */
    private Integer enrollType;

    /**
     * 适用行政班ID集合JSON
     */
    @TableField("applicable_class_ids")
    private String applicableClassIds;

    /**
     * 适用年级ID集合JSON
     */
    @TableField("applicable_grade_ids")
    private String applicableGradeIds;

    /**
     * 邀请报名时是否需要家长确认：0否 1是
     */
    private Integer parentConfirmRequired;

    /**
     * 公开报名性别要求：0不限 1男 2女
     */
    private Integer genderRequirement;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 已报名人数
     */
    private Integer enrolledCount;

    /** 查询侧批量装载的年级限额，不参与课程班持久化。 */
    @TableField(exist = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<AsCourseGradeQuotaEntity> gradeQuotaRows;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教室ID，多个用英文逗号分隔
     */
    private String placeId;

    /**
     * 课程介绍
     */
    private String intro;

    /**
     * 课程班状态：1未开始 2进行中 3已结束 4停用
     */
    private Integer classStatus;

    // 课程封面
    private Long courseFile;
}
