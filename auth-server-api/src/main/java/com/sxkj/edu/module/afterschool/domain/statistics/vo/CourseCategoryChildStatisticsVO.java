package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseCategoryChildStatisticsVO {

    @ApiModelProperty("课程类别id")
    private Long categoryId;

    @ApiModelProperty("课程类别名称")
    private String courseCategoryName;

    @ApiModelProperty("课程类别数量")
    private Integer courseCategoryCount;
}
