package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生考勤汇总统计VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("学生考勤汇总统计")
public class AsAttendanceStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("应出勤人数")
    private Integer shouldAttendCount;

    @ApiModelProperty("实际出勤人数")
    private Integer actualAttendCount;

    @ApiModelProperty("迟到人数")
    private Integer lateCount;

    @ApiModelProperty("早退人数")
    private Integer earlyLeaveCount;

    @ApiModelProperty("缺勤人数")
    private Integer absentCount;

    @ApiModelProperty("请假人数")
    private Integer leaveCount;

    @ApiModelProperty("出勤率（百分比）")
    private String attendanceRate;
}
