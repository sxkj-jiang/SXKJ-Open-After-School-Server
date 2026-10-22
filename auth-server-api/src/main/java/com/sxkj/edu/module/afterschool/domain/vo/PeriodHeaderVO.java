package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课表表头-节次
 */
@Data
public class PeriodHeaderVO {

    @ApiModelProperty(value = "节次序号，如：8")
    private Integer sectionNo;

    @ApiModelProperty(value = "节次名称，如：第八节")
    private String sectionName;

    @ApiModelProperty(value = "时间段，如：16:30-17:15")
    private String timeRange;
}
