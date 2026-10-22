package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 课表表头-周
 */
@Data
public class WeekHeaderVO {

    @ApiModelProperty(value = "星期几(1-7)")
    private Integer weekDay;

    @ApiModelProperty(value = "星期展示名称，如：周一")
    private String weekDayName;

    @ApiModelProperty(value = "该周下节次数量（前端合并单元格用）")
    private int periodCount;

    @ApiModelProperty(value = "节次列表")
    private List<PeriodHeaderVO> periods;
}
