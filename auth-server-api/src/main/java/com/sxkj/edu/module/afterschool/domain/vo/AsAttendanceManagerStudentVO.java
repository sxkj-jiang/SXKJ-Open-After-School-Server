package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理端学生考勤记录VO
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("管理端学生考勤记录VO")
public class AsAttendanceManagerStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("考勤id")
    private Long id;

    @ApiModelProperty("学生去向预警ID")
    private Long warningRecordId;

    @ApiModelProperty("班级id")
    private Long classId;

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("课次id")
    private Long sessionId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

    @ApiModelProperty("考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假")
    private Integer attendanceStatus;

    @ApiModelProperty("考勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("选课方式：1-公开报名，2-邀请报名，3-行政班安排")
    private Integer enrollType;

    @ApiModelProperty("选课方式描述")
    private String enrollTypeDesc;

    @ApiModelProperty("课程活动名称")
    private String serviceName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("上课日期，格式：2026-04-01")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionTime;

    @ApiModelProperty("上课时间格式：14:00-15:00")
    private String classTime;

    @ApiModelProperty("上课时间格式：2026-04-01 14:00-15:00")
    private String classResTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("异常原因描述")
    private String exceptionDesc;

    @ApiModelProperty("处理方式：1课后留堂 2联系家长 3约谈学生 4其他")
    private Integer handleType;

    @ApiModelProperty(value = "预计迟到时间（分钟）")
    private Integer lateMinutes;

    @ApiModelProperty("请假原因")
    private String leaveReason;

    @ApiModelProperty(value = "学期名称")
    private String termName;

    @ApiModelProperty("学期ID")
    private Long termId;

}
