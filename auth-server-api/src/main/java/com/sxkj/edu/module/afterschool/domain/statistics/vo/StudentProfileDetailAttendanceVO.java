package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentProfileDetailAttendanceVO {

    @ApiModelProperty("出勤率")
    private String attendanceRate;

    @ApiModelProperty("请假次数")
    private Integer leaveCount;

    @ApiModelProperty("迟到次数")
    private Integer lateCount;

    @ApiModelProperty("早退次数")
    private Integer earlyLeaveCount;

    @ApiModelProperty("缺勤次数")
    private Integer absentCount;

    public StudentProfileDetailAttendanceVO(){
        this.attendanceRate = "0.00%";
        this.leaveCount = 0;
        this.lateCount = 0;
        this.earlyLeaveCount = 0;
        this.absentCount = 0;
    }

}
