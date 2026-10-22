package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家长端课程时间安排项
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@ApiModel("家长端课程时间安排项")
public class AsGuardianScheduleItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("星期几：1周一至7周日")
    private Integer weekDay;

    @ApiModelProperty("星期展示名称，如每周一")
    private String weekDayName;

    @ApiModelProperty("开始时间，HH:mm")
    private String startTime;

    @ApiModelProperty("结束时间，HH:mm")
    private String endTime;

    @ApiModelProperty("课次日期，yyyy-MM-dd")
    private Date sessionDate;
}
