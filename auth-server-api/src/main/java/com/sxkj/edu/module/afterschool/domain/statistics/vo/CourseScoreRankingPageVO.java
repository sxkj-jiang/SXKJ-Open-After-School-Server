package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程评分排行-课程评价分页 返回参数
 */
@Data
public class CourseScoreRankingPageVO {

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程类型")
    private String categoryName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("评价平均分")
    private BigDecimal averageScore;

    @ApiModelProperty("评价数量")
    private Integer evaluationCount;

    @ApiModelProperty("参与率")
    private String participationRate;
}
