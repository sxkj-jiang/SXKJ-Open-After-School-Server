package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后类别/学习模块实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_course_category")
public class AsCourseCategoryEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 父类别id
     */
    private Long parentId;

    /**
     * 类别编码
     */
    private String categoryCode;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 启用状态：1启用 0停用
     */
    private Integer enabledStatus;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 备注
     */
    private String remark;
}
