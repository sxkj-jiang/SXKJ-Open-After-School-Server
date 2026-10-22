package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生合并记录VO（出勤+报名+请假+调课）
 */
@Data
@ApiModel("学生合并记录（出勤/报名/请假/调课）")
public class AsStudentMergedRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("出勤记录列表")
    private List<AttendanceItem> attendanceRecords = new ArrayList<>();

    @ApiModelProperty("报名记录列表")
    private List<EnrollmentItem> enrollmentRecords = new ArrayList<>();

    @ApiModelProperty("请假记录列表")
    private List<LeaveItem> leaveRecords = new ArrayList<>();

    @ApiModelProperty("调课记录列表")
    private List<AdjustItem> adjustRecords = new ArrayList<>();

    /**
     * 出勤记录项
     */
    @Data
    @ApiModel("学生出勤记录项")
    public static class AttendanceItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("学号")
        private String studentNo;

        @ApiModelProperty("课程日期")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date sessionDate;

        @ApiModelProperty("星期几：1周一 2周二 ... 7周日")
        private Integer weekDay;

        @ApiModelProperty("出勤状态：1出勤 2迟到 3早退 4缺勤 5请假")
        private Integer attendanceStatus;

        @ApiModelProperty("去向：1课程班 2回家 3培训机构 4其他")
        private Integer destinationType;

        @ApiModelProperty("备注")
        private String remark;
    }

    /**
     * 报名记录项
     */
    @Data
    @ApiModel("学生报名记录项")
    public static class EnrollmentItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("学号")
        private String studentNo;

        @ApiModelProperty("报名时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date enrollTime;

        @ApiModelProperty("课程班名称")
        private String courseClassName;

        @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
        private Integer enrollType;

        @ApiModelProperty("报名方式描述")
        private String enrollTypeDesc;

        @ApiModelProperty("上课时间（开始时间-结束时间）")
        private String classTime;

        @ApiModelProperty("报名状态")
        private Integer enrollStatus;

        @ApiModelProperty("报名状态描述")
        private String enrollStatusDesc;
    }

    /**
     * 请假记录项
     */
    @Data
    @ApiModel("学生请假记录项")
    public static class LeaveItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("学号")
        private String studentNo;

        @ApiModelProperty("请假时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date leaveTime;

        @ApiModelProperty("课程班名称")
        private String courseClassName;

        @ApiModelProperty("请假类型：1病假 2事假 3其他")
        private Integer leaveType;

        @ApiModelProperty("请假类型描述")
        private String leaveTypeDesc;

        @ApiModelProperty("课次日期")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date sessionDate;

        @ApiModelProperty("请假人用户ID")
        private Long applicantUserId;
    }

    /**
     * 调课记录项
     */
    @Data
    @ApiModel("学生调课记录项")
    public static class AdjustItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("学号")
        private String studentNo;

        @ApiModelProperty("调课日期（申请时间）")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date adjustDate;

        @ApiModelProperty("原课程班名称")
        private String fromCourseClassName;

        @ApiModelProperty("新课程班名称")
        private String toCourseClassName;

        @ApiModelProperty("调课原因")
        private String reason;

        @ApiModelProperty("课次日期")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date sessionDate;

        @ApiModelProperty("操作人姓名")
        private String applicantName;
    }
}
