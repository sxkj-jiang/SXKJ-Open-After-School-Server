package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生出勤趋势子项VO
 *
 * @author system
 * @date 2026-06-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("学生出勤趋势子项")
public class StudentAttendanceTrendItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份（如：2026年1月）")
    private String month;

    @ApiModelProperty("出勤率（如：85.5%）")
    private String attendanceRate;
}
