package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseStatisticsAO {

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：0-全部，1-课程分布，2-出勤率趋势，3-课程评价，4-总课时统计，5-课时完成情况，6-课程评分排行", required = true)
    private Integer type;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID",required = true)
    private Long termId;

    @ApiModelProperty(value = "课程评价-课程类别ID")
    private Long kcpjCategoryId;

    @ApiModelProperty(value = "总课时统计-课程类别ID")
    private Long zkstjCategoryId;

    @ApiModelProperty(value = "课时完成情况-课程类别ID")
    private Long kswcqkCategoryId;

    @ApiModelProperty(value = "课程评分排行-课程类别ID")
    private Long kcpfphCategoryId;
}
