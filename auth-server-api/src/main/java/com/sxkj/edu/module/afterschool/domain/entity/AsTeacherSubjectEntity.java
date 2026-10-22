package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 学科教师关联实体
 *
 * @author system
 * @date 2026-08-26
 */
@Data
@TableName("t_teacher_subject")
public class AsTeacherSubjectEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教师ID（关联教师管理表）
     */
    private Long teacherId;

    /**
     * 学科ID（关联 t_as_subject）
     */
    private Long subjectId;

    /**
     * 学校ID
     */
    private Long schoolId;
}
