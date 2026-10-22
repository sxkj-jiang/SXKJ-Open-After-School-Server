package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 课程评分排行-数据概览 请求参数
 */
@Data
public class CourseScoreRankingOverviewAO {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;

    @ApiModelProperty(value = "课程类别ID")
    private Long categoryId;
}
