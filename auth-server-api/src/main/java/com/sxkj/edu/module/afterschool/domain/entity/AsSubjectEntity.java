package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 学科管理实体
 *
 * @author system
 * @date 2026-08-26
 */
@Data
@TableName("t_as_subject")
public class AsSubjectEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学科编码
     */
    private String subjectCode;

    /**
     * 学科名称
     */
    private String subjectName;

    /**
     * 学科描述
     */
    private String subjectDesc;

    /**
     * 年级id
     */
    private Long gradeId;

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 是否启用 0-否 1-是
     */
    private Integer enabled;
}
