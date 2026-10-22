package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AttendanceTrendStatisticsVO {

    @ApiModelProperty(value = "第几周")
    private String week;

    @ApiModelProperty("各年级集合数据")
    private List<StatisticsCommonCountVO> gradeData;

}
