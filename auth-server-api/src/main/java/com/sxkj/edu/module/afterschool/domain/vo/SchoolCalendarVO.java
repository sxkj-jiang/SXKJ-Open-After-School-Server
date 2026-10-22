package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 校历VO
 *
 * @author system
 * @date 2026-06-14
 */
@Data
@ApiModel(value = "校历VO", description = "校历查询返回结果")
public class SchoolCalendarVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期总周数")
    private Integer totalWeeks;

    @ApiModelProperty("当前周次（第几周）")
    private Integer currentWeek;

    @ApiModelProperty("当前周下的日历集合")
    private List<CalendarDayVO> calendarDays;
}
