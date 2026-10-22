package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程班排课规则实体。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@TableName("t_as_course_schedule_rule")
public class AsCourseScheduleRuleEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 课后服务ID
     */
    private Long serviceId;

    /**
     * 课程班ID
     */
    private Long courseClassId;

    /**
     * 规则类型：1周期 2自定义日期
     */
    private Integer ruleType;

    /**
     * 周期规则星期集合JSON，如[1,3,5]
     */
    @TableField("week_days_json")
    private String weekDaysJson;

    /**
     * 自定义日期
     */
    private Date sessionDate;

    /**
     * 来源作息/课后时间段ID
     */
    private Long schedulePeriodId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 规则覆盖地点，空时使用课程班地点
     */
    private String placeName;

    /**
     * 规则覆盖教师，空时使用课程班教师
     */
    private Long teacherId;
}
