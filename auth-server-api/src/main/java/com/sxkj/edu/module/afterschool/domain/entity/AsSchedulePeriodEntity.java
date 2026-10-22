package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作息节次/课后时间段实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_schedule_period")
public class AsSchedulePeriodEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 方案ID
     */
    private Long templateId;

    /**
     * 时间段类型：1普通节次 2课后时间段
     */
    private Integer periodType;

    /**
     * 星期几：1周一至7周日，课后时间可为空
     */
    private Integer dayOfWeek;

    /**
     * 课后时间频率JSON，如周一至周五
     */
    @TableField("day_scope_json")
    private String dayScopeJson;

    /**
     * 适用年级集合JSON
     */
    @TableField("grade_scope_json")
    private String gradeScopeJson;

    /**
     * 第几节
     */
    private Integer sectionNo;

    /**
     * 节次名称
     */
    private String sectionName;

    /**
     * 普通节次时间段：1上午 2下午
     */
    private Integer timeBlock;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 排序号
     */
    private Integer sortNo;
}
