package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("请假申请视图对象")
public class AsLeaveRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("请假ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("学生ID")
    private Long studentId;
    @ApiModelProperty("学生姓名")
    private String studentName;
    @ApiModelProperty("学生学号")
    private String studentNo;
    @ApiModelProperty("监护人用户ID")
    private Long guardianUserId;
    @ApiModelProperty("监护人姓名")
    private String guardianName;
    @ApiModelProperty("申请人角色")
    private Integer applicantRole;
    @ApiModelProperty("申请人角色描述")
    private String applicantRoleDesc;
    @ApiModelProperty("申请人用户ID")
    private Long applicantUserId;
    @ApiModelProperty("申请人姓名")
    private String applicantName;
    @ApiModelProperty("请假类型")
    private Integer leaveType;
    @ApiModelProperty("请假类型描述")
    private String leaveTypeDesc;
    @ApiModelProperty("开始时间")
    private Date leaveStartTime;
    @ApiModelProperty("结束时间")
    private Date leaveEndTime;
    @ApiModelProperty("请假天数")
    private BigDecimal leaveDays;
    @ApiModelProperty("涉及课次JSON")
    private String leaveSessionsJson;
    @ApiModelProperty("请假原因")
    private String leaveReason;
    @ApiModelProperty("审批状态")
    private Integer approveStatus;
    @ApiModelProperty("审批状态描述")
    private String approveStatusDesc;


    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("班级")
    private String className;

    @ApiModelProperty("发起人")
    private String initiatorName;

    @ApiModelProperty("班主任")
    private String classNameTeacher;

}
