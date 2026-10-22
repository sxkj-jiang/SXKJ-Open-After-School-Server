package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseCategoryStatisticsVO {

    @ApiModelProperty("大类课程名称")
    private String courseCategoryName;

    @ApiModelProperty("大类课程数量")
    private Integer courseCategoryCount;

    @ApiModelProperty("大类课程子类列表")
    private List<CourseCategoryChildStatisticsVO> childrenList;
}
