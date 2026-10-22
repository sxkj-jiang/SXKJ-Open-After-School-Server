package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 教师查询考勤记录返回VO
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("教师查询考勤记录返回VO")
public class AsAttendanceRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课程活动名称")
    private String serviceName;

    @ApiModelProperty("上课时间")
    private String classTime;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("学生考勤汇总统计信息")
    private AttendanceSummaryVo summary;

    @ApiModelProperty("学生考勤异常详细名单")
    private List<AttendanceAbnormalDetail> abnormalDetails;

    /**
     * 考勤汇总统计
     */
    @Data
    @ApiModel("考勤汇总统计")
    public static class AttendanceSummaryVo implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("应出勤人数")
        private Integer attendanceSumCount;

        @ApiModelProperty("实际出勤人数")
        private Integer attendanceCount;

        @ApiModelProperty("请假人数")
        private Integer leaveCount;

        @ApiModelProperty("迟到人数")
        private Integer lateCount;

        @ApiModelProperty("早退人数")
        private Integer earlyLeaveCount;

        @ApiModelProperty("缺勤人数")
        private Integer absentCount;

        @ApiModelProperty("出勤率")
        private String attendanceRate;
    }

    /**
     * 考勤异常详细名单
     */
    @Data
    @ApiModel("考勤异常详细名单")
    public static class AttendanceAbnormalDetail implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("异常类型：缺勤/早退/请假")
        private String abnormalType;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("班级名称+年级")
        private String className;

        @ApiModelProperty("学号")
        private String studentNo;

        @ApiModelProperty("请假时间")
        private String leaveTime;

        @ApiModelProperty("备注")
        private String remark;
    }
}
