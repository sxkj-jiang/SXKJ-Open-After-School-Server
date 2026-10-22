package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 课次学生考勤记录VO
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("课次学生考勤记录VO")
public class AsAttendanceStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生课次ID")
    private Long sessionId;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("用户头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("用户头像URL")
    private String avatarFileUrl;

    @ApiModelProperty("课次日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("开课时间")
    private String startTime;

    @ApiModelProperty("下课时间")
    private String endTime;

    @ApiModelProperty("考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假")
    private Integer attendanceStatus;

    @ApiModelProperty(value = "去向类型：1课程班 2回家 3培训机构 4留班 5行政课 6其他", required = true)
    private Integer destinationType;

    @ApiModelProperty("学生去向类型名称")
    private String destinationName;

    @ApiModelProperty("考勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("班主任用户ID")
    private Long headTeacherUserId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("年级班级名称")
    private String gradeClassName;

    @ApiModelProperty("请假ID")
    private Long leaveId;

    @ApiModelProperty("学生去向预警ID")
    private Long warningRecordId;

    @ApiModelProperty("预警状态 0未处理 1处理中 2已处理 3已关闭")
    private Integer warningStatus;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("异常原因描述")
    private String exceptionDesc;

}
