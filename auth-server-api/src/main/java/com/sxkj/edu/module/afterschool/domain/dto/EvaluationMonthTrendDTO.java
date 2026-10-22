package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 评价月度趋势统计DTO（SQL聚合结果）
 */
@Data
public class EvaluationMonthTrendDTO {

    @ApiModelProperty("月份")
    private String month;

    @ApiModelProperty("家长评价平均分")
    private BigDecimal parentAvgScore;

    @ApiModelProperty("管理员评价平均分")
    private BigDecimal adminAvgScore;

    public EvaluationMonthTrendDTO() {
        this.parentAvgScore = BigDecimal.ZERO;
        this.adminAvgScore = BigDecimal.ZERO;
    }
}
