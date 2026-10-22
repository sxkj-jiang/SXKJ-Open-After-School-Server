package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生考勤汇总统计查询参数
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("学生考勤汇总统计查询参数")
public class AsAttendanceStatisticsQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;

    @ApiModelProperty(value = "查询日期（格式：yyyy-MM-dd）", required = true)
    private String sessionDate;
}
