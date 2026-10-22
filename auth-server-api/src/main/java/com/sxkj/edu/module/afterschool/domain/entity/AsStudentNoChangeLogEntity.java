package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生学号纠错审计实体。
 */
@Data
@TableName("t_as_student_no_change_log")
public class AsStudentNoChangeLogEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校。
     */
    private Long schoolId;

    /**
     * 学生稳定身份编号。
     */
    private Long studentIdentityId;

    /**
     * 修改前学号。
     */
    private String oldStudentNo;

    /**
     * 修改后学号。
     */
    private String newStudentNo;

    /**
     * 修改原因。
     */
    private String changeReason;
}
