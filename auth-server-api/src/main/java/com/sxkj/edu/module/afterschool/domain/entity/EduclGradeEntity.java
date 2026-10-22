package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 教务年级实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_educl_grade")
public class EduclGradeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校，对应 t_school_manage.id
     */
    private Long schoolId;

    /**
     * 年级编码
     */
    private String gradeCode;

    /**
     * 阶段：1-小学/2-初中/3-高中等
     */
    private Integer schoolStage;

    /**
     * 年级名称
     */
    private String gradeName;

    /**
     * 年级主任账号，对应 t_as_teacher.id
     */
    private Long directorUserId;

    /**
     * 年级主任名称，便于导入展示
     */
    private String directorName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序号
     */
    private Integer sortNo;
}
