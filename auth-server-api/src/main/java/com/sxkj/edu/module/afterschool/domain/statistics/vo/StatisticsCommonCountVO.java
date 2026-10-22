package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsCommonCountVO {

    @ApiModelProperty(value = "统计名称")
    private String name;

    @ApiModelProperty(value = "统计值")
    private Integer value;

    @ApiModelProperty(value = "统计小数值")
    private BigDecimal sValue;

    @ApiModelProperty(value = "统计比率")
    private String ratio;

    public StatisticsCommonCountVO() {
        this.value = 0;
        this.ratio = "0.00%";
    }
}
