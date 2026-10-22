package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticKeyWordPercentageVO {

    @ApiModelProperty(value = "统计词")
    private String keyword;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "小数数量")
    private BigDecimal doubleCount;

    @ApiModelProperty(value = "百分比（%）")
    private String percentage;
}
