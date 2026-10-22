package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 家长查询考勤汇总统计VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
public class AsParentAttendanceSummaryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生参与的课程班总数")
    private Integer totalCourseClass;

    @ApiModelProperty("出勤数")
    private Integer attendanceCount;

    @ApiModelProperty("迟到数")
    private Integer lateCount;

    @ApiModelProperty("请假数")
    private Integer leaveCount;

    @ApiModelProperty("缺勤数")
    private Integer absentCount;

    @ApiModelProperty("早退数")
    private Integer earlyLeaveCount;
}
