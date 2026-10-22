package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生学期内年级、班级和学籍变更记录实体。
 */
@Data
@TableName("t_as_student_term_change_log")
public class AsStudentTermChangeLogEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校。
     */
    private Long schoolId;

    /**
     * 所属学期。
     */
    private Long termId;

    /**
     * 学生学期快照编号。
     */
    private Long studentId;

    /**
     * 学生稳定身份编号。
     */
    private Long studentIdentityId;

    /**
     * 变更生效时间。
     */
    private Date effectiveTime;

    /**
     * 变更前年级。
     */
    private Long oldGradeId;

    /**
     * 变更后年级。
     */
    private Long newGradeId;

    /**
     * 变更前班级。
     */
    private Long oldClassId;

    /**
     * 变更后班级。
     */
    private Long newClassId;

    /**
     * 变更前学籍状态。
     */
    private Integer oldSchoolRollStatus;

    /**
     * 变更后学籍状态。
     */
    private Integer newSchoolRollStatus;

    /**
     * 变更原因。
     */
    private String changeReason;

    /**
     * 生效状态：0待生效，1已生效，2生效失败，3正在生效。
     */
    private Integer applyStatus;

    /**
     * 实际生效时间。
     */
    private Date applyTime;

    /**
     * 生效失败原因。
     */
    private String failureReason;
}
