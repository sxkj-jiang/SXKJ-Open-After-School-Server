package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseStatisticsVO {

    @ApiModelProperty("课程分布集合数据")
    private List<StatisticsCommonCountVO> kcfbData;

    @ApiModelProperty("出勤率趋势集合数据")
    private List<StatisticsCommonCountVO> cqlqsData;

    @ApiModelProperty("课程评价集合数据")
    private List<StatisticsCommonCountVO> kcpjData;

    @ApiModelProperty("总课时统计集合数据")
    private List<StatisticsCommonCountVO> zkstjData;

    @ApiModelProperty("课时完成情况集合数据")
    private List<StatisticsLessonFinishVO> kswcqkData;

    @ApiModelProperty("课程评分排行集合数据")
    private List<StatisticsCourseRankingVO> kcpfphData;
}
