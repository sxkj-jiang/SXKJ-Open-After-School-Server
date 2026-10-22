package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生月度出勤统计VO
 *
 * @author system
 * @date 2026-06-10
 */
@Data
@ApiModel("学生月度出勤统计VO")
public class StudentMonthlyAttendanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份（如：2026年06月）")
    private String month;

    @ApiModelProperty("总课时数")
    private Integer totalHours;

    @ApiModelProperty("出勤课时数")
    private Integer attendanceHours;

    @ApiModelProperty("缺勤请假课时")
    private Integer absenceHours;
}
