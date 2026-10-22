package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsCourseRankingVO {

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("课程评分平均分")
    private BigDecimal courseAverageScore;

    @ApiModelProperty("出勤率")
    private String attendanceRate;
}
