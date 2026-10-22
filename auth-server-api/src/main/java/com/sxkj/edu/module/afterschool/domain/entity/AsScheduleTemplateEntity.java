package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 作息/课后时间方案实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_schedule_template")
public class AsScheduleTemplateEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 方案类型：1作息管理 2课后时间管理
     */
    private Integer templateType;

    /**
     * 方案名称
     */
    private String templateName;

    /**
     * 启用状态：1启用 0停用
     */
    private Integer enabledStatus;

    /**
     * 学校名称(原型详情展示)
     */
    private String schoolName;

    /**
     * 备注
     */
    private String remark;
}
