package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 课程评分排行-数据概览 返回参数
 */
@Data
public class CourseScoreRankingOverviewVO {

    @ApiModelProperty("平均评价得分")
    private BigDecimal averageScore;

    @ApiModelProperty("提交评价总数")
    private Integer totalEvaluationCount;

    @ApiModelProperty("评价参与率")
    private String participationRate;

    @ApiModelProperty("评价分布分析数据")
    private List<StatisticsCommonCountVO> scoreDistribution;
}
