package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TeacherStatisticsVO {

    @ApiModelProperty("出勤率分布集合数据")
    private List<StatisticsCommonCountVO> cqlData;

    @ApiModelProperty("月度趋势集合数据")
    private List<StatisticsCommonCountVO> ydqsData;

    @ApiModelProperty("课程类型分布集合数据")
    private List<StatisticsCommonCountVO> kclxData;

    @ApiModelProperty("月度课时趋势集合数据")
    private List<StatisticsCommonCountVO> ydksData;

    @ApiModelProperty("点到率集合数据")
    private List<StatisticsCommonCountVO> ddlData;

    @ApiModelProperty("评价分数集合数据")
    private List<StatisticsCommonCountVO> pjfsData;
}
