package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

/** 公开课程班的年级限额，唯一键为课程班与年级。 */
@Data
@TableName("t_as_course_grade_quota")
public class AsCourseGradeQuotaEntity extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Long schoolId;
    private Long courseClassId;
    private Long gradeId;
    private Integer capacity;
    private Integer enrolledCount;
}
