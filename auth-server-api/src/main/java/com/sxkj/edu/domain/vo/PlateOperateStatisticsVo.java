package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlateOperateStatisticsVo {

    @ApiModelProperty("关键指标概览统计")
    private KeyMetricsStatisticsVo keyMetricsVo;

    @ApiModelProperty("流量趋势统计")
    private List<PlateTrafficTrendStatisticsVo> trafficTrendVoList;

    @ApiModelProperty("平台使用时间统计")
    private List<PlateOperateTimeStatisticsVo> timeTjVoList;

    @ApiModelProperty("平台每日用户登录统计")
    private List<PlateUserLoginStatisticsVo> loginUserVoList;

    @ApiModelProperty("平台使用频率统计")
    private List<PlateOperateFrequencyStatisticsVo> frequencyTjVoList;

    @ApiModelProperty("访问端占比统计")
    private List<StatisticKeyWordPercentageVO> visitorShareVoList;

    @ApiModelProperty("设备分布占比统计")
    private List<StatisticKeyWordPercentageVO> equipDistributeVoList;

}
