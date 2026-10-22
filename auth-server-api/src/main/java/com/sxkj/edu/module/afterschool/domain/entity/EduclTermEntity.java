package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教务学期/校历实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_educl_term")
public class EduclTermEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校，对应 t_school_manage.id
     */
    private Long schoolId;

    /**
     * 学期编码/序号
     */
    private String termCode;

    /**
     * 学期名称
     */
    private String termName;

    /**
     * 学年，如2023-2024学年
     */
    private String schoolYear;

    /**
     * 1第一学期 2第二学期 3其他
     */
    private Integer termType;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 总周数
     */
    private Integer totalWeeks;

    /**
     * 1启用 0停用
     */
    private Integer termStatus;
}
