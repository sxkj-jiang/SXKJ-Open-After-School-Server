package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("课程质量统计VO")
public class CourseQualityStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生家长平均评分")
    private BigDecimal parentAvgScore;

    @ApiModelProperty("家长评分较上学期变化率")
    private String parentAvgScoreChangeRate;

    @ApiModelProperty("管理员平均评分")
    private BigDecimal adminAvgScore;

    @ApiModelProperty("管理员评分较上学期变化率")
    private String adminAvgScoreChangeRate;

    @ApiModelProperty("学生参与度")
    private String participationRate;

    @ApiModelProperty("参与度较上学期变化率")
    private String participationRateChangeRate;

    @ApiModelProperty("已评估课程数")
    private Integer evaluatedCourseCount;

    @ApiModelProperty("完成率")
    private String completionRate;

    @ApiModelProperty("完成率较上学期变化率")
    private String completionRateChangeRate;
}
