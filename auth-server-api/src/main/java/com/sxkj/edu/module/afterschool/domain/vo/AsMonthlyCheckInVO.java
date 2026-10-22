package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 每月打卡趋势VO
 *
 * @author system
 * @date 2026-06-19
 */
@Data
public class AsMonthlyCheckInVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份，格式yyyy-MM")
    private String month;

    @ApiModelProperty("打卡次数")
    private Integer checkInCount;

    @ApiModelProperty("应打卡次数")
    private Integer shouldCheckInCount;

    @ApiModelProperty("打卡率")
    private String checkInRate;

}
