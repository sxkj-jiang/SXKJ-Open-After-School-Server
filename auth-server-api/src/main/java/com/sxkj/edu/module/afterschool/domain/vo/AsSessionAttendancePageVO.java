package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程班课次考勤分页VO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsSessionAttendancePageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课次日期")
    private String sessionDate;

    @ApiModelProperty("上课时间（开始时间-结束时间）")
    private String classTime;

    @ApiModelProperty("课程班总人数")
    private Integer totalStudents;

    @ApiModelProperty("出勤人数（正常、迟到、早退）")
    private Integer presentCount;

    @ApiModelProperty("缺勤人数")
    private Integer absentCount;

    @ApiModelProperty("迟到人数")
    private Integer lateCount;

    @ApiModelProperty("早退人数")
    private Integer earlyLeaveCount;

    @ApiModelProperty("请假人数")
    private Integer leaveCount;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;
}
