package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程班排课规则入参。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课程班排课规则入参")
public class AsCourseScheduleRuleAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规则类型：weekly周期 date自定义日期")
    private String ruleType;

    @ApiModelProperty("周期规则星期集合，1周一到7周日")
    private List<Integer> weekDays;

    @ApiModelProperty("自定义上课日期")
    private Date sessionDate;

    @ApiModelProperty("来源作息/课后时间段ID")
    private Long schedulePeriodId;

    @ApiModelProperty("开始时间，HH:mm；选择节次时可不传，由节次配置带出")
    private String startTime;

    @ApiModelProperty("结束时间，HH:mm；选择节次时可不传，由节次配置带出")
    private String endTime;

    @ApiModelProperty("规则覆盖地点ID，空时取课程班地点")
    private String placeId;

    @ApiModelProperty("规则覆盖教师，空时取课程班教师")
    private Long teacherId;
}
