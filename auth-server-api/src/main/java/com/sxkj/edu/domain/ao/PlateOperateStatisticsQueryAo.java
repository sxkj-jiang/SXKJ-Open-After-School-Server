package com.sxkj.edu.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PlateOperateStatisticsQueryAo {

    @ApiModelProperty(value = "查询统计类型:0-全查、1-关键指标概览、2-流量趋势图、3-平台使用时间统计、4-每日登录用户访问量、5-平台使用频率统计、6-访问端占比、7-设备分布",required = true)
    private Integer queryType;

    /*关键指标概览-搜索条件*/
    @ApiModelProperty(value = "关键指标概览-平台类型: 平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String keyMetricsPlateType;

    @ApiModelProperty(value = "关键指标概览-时间段类型: 1-今日 2-本周 3-本月")
    private Integer keyMetricsTimeType;

    @ApiModelProperty(value = "关键指标概览-开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date keyMetricsStartDate;

    @ApiModelProperty(value = "关键指标概览-结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date keyMetricsEndDate;

    /*流量趋势-搜索条件*/
    @ApiModelProperty(value = "流量趋势-平台类型: 平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String trafficTrendPlateType;

    @ApiModelProperty(value = "流量趋势-时间段类型: 1-今日 2-本周 3-本月",required = true)
    private Integer trafficTrendTimeType;

    @ApiModelProperty(value = "流量趋势-开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trafficTrendStartDate;

    @ApiModelProperty(value = "流量趋势-结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trafficTrendEndDate;

    /*平台使用时间-搜索条件*/
    @ApiModelProperty(value = "平台使用时间-用户类型 1-超级管理员 2-学生 3-教师  4-学校管理员 5-班主任",required = true)
    private Integer usePlateUserType;

    @ApiModelProperty(value = "平台使用时间-时间段类型:1-今日、2-本周、3-本月")
    private Integer usePlateTimeType;

    @ApiModelProperty(value = "平台使用时间-开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date usePlateStartDate;

    @ApiModelProperty(value = "平台使用时间-结束日期 格式:yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date usePlateEndDate;

    /*每日登录用户访问量-搜索条件*/
    @ApiModelProperty(value = "每日登录用户访问量-平台类型 平台网页端 PTPC,管理端 JWPC,平台小程序 XCX（不传值就是查全部）")
    private String userLoginPlateType;

    @ApiModelProperty(value = "每日登录用户访问量-开始日期  格式:yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date userLoginStartDate;

    @ApiModelProperty(value = "每日登录用户访问量-结束日期 格式:yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date userLoginEndDate;

    /*平台使用频率-搜索条件*/
    @ApiModelProperty(value = "平台使用频率-平台类型:平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String useFrequencyPlateType;

    @ApiModelProperty(value = "平台使用频率-时间段类型:1-今日、2-本周、3-本月")
    private Integer useFrequencyTimeType;

    @ApiModelProperty(value = "平台使用频率-开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date useFrequencyStartDate;

    @ApiModelProperty(value = "平台使用频率-结束日期 格式:yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date useFrequencyEndDate;

    /*设备分布-搜索条件*/
    @ApiModelProperty(value = "设备分布-平台类型:平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String visitPlateType;


}
