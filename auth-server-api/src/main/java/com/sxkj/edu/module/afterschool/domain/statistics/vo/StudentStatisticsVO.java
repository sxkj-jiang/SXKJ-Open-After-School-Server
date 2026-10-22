package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StudentStatisticsVO {

    @ApiModelProperty("本次统计所属学期ID")
    private Long termId;

    @ApiModelProperty("本次统计所属学期名称")
    private String termName;

    @ApiModelProperty("学生考勤分布集合数据")
    private List<StatisticsCommonCountVO> kqfbData;

    @ApiModelProperty("月度趋势集合数据")
    private List<StatisticsCommonCountVO> ydqsData;

    @ApiModelProperty("特色课程分布集合数据")
    private List<StatisticsCommonCountVO> tskcData;

    @ApiModelProperty("行政课程分布集合数据")
    private List<StatisticsCommonCountVO> xzkcData;

    @ApiModelProperty("兴趣发展数据")
    private StatisticsXqfzVO xqfzData;

    @ApiModelProperty("性别分布集合数据")
    private List<StatisticsCommonCountVO> xbfbData;

    @ApiModelProperty("学生选课分布集合数据")
    private List<StatisticsCommonCountVO> xsxkData;
}
