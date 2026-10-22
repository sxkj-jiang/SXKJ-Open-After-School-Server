package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("请假申请视图对象")
public class AsLeaveRequestManagerQueryPageVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("请假ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("年级id")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("学生姓名")
    private String studentName;
    @ApiModelProperty("学生学号")
    private String studentNo;
    @ApiModelProperty("监护人用户ID")
    private Long guardianUserId;
    @ApiModelProperty("监护人姓名")
    private String guardianName;

    @ApiModelProperty("申请人用户ID")
    private Long applicantUserId;
    @ApiModelProperty("申请人姓名")
    private String applicantName;
    @ApiModelProperty("请假类型 1病假 2事假 3其他")
    private Integer leaveType;
    @ApiModelProperty("申请人角色：1家长 2班主任 3管理端")
    private Integer applicantRole;
    @ApiModelProperty("请假类型描述")
    private String leaveTypeDesc;

    @ApiModelProperty("审批状态：0待审批 1已通过 2已驳回 3已撤回")
    private Integer approveStatus;

    @ApiModelProperty("请假天数")
    private BigDecimal leaveDays;
    @ApiModelProperty("涉及课次JSON")
    private String leaveSessionsJson;

    @ApiModelProperty("请假时间")
    private String leaveTime;

    @ApiModelProperty("申请时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("请假开始时间：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date leaveStartTime;

    @ApiModelProperty("请假结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date leaveEndTime;

    @ApiModelProperty("年级")
    private String gradeName;

    @ApiModelProperty("班级")
    private String className;

    @ApiModelProperty("发起人")
    private String initiatorName;

    @ApiModelProperty("班主任")
    private String classNameTeacher;

    @ApiModelProperty("请假原因")
    private String leaveReason;

}
