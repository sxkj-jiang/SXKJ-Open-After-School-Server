package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 日历日期VO
 *
 * @author system
 * @date 2026-06-14
 */
@Data
@ApiModel(value = "日历日期VO", description = "校历中每一天的详细信息")
public class CalendarDayVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日期，格式 yyyy-MM-dd")
    private String date;

    @ApiModelProperty("星期几：1-周一，7-周日")
    private Integer weekDay;

    @ApiModelProperty("星期几名称，如 周一、周二")
    private String weekDayName;

    @ApiModelProperty("是否当天")
    private Boolean isToday;

    @ApiModelProperty("节假日名称，非节假日时为空")
    private String holidayName;
}
