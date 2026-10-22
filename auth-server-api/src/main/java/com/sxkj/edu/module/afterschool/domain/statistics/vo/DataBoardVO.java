package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DataBoardVO {

    @ApiModelProperty("学生总数")
    private Integer totalStudentCount;

    @ApiModelProperty("课后课程数量")
    private Integer totalCourseCount;

    @ApiModelProperty("学生参与率")
    private String totalParticipationRate;

    @ApiModelProperty("学生出勤率")
    private String totalAttendanceRate;

    @ApiModelProperty("教师总数")
    private Integer totalTeacherCount;

    @ApiModelProperty("教师出勤率")
    private String totalTeacherAttendanceRate;

    @ApiModelProperty("课时总数")
    private Integer totalClassHourCount;

    @ApiModelProperty("教师平均分")
    private BigDecimal totalTeacherAverageScore;

    @ApiModelProperty("年级人数分布集合数据")
    private List<StatisticsCommonCountVO> njrsData;

    @ApiModelProperty("课程类型人数分布集合数据")
    private List<StatisticsCommonCountVO> kslxrsData;

    @ApiModelProperty("学生每周出勤趋势")
    private List<StatisticsCommonCountVO> xszcqData;

    @ApiModelProperty("教师每周出勤趋势")
    private List<StatisticsCommonCountVO> jszcqData;
}
