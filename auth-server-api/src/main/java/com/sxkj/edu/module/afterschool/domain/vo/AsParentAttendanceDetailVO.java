package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家长查询考勤详情记录VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
public class AsParentAttendanceDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("考勤记录ID")
    private Long id;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("出勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("出勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("原因")
    private String leaveReason;
}
