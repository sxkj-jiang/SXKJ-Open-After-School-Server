package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师行政班级绑定实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_teacher_class")
public class AsTeacherClassEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 行政班ID，对应 t_educl_class.id
     */
    private Long classId;

    /**
     * 绑定类型：1班主任 2任课关联
     */
    private Integer bindType;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;
}
