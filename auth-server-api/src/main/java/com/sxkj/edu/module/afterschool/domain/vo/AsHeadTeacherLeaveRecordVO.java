package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 班主任查询班级学生请假记录VO（含学生信息）
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("班主任查询班级学生请假记录VO")
public class AsHeadTeacherLeaveRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("请假ID")
    private Long id;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("请假开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveStartDate;

    @ApiModelProperty("请假结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date leaveEndDate;

    @ApiModelProperty("申请日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @ApiModelProperty("请假类型：1病假 2事假 3其他")
    private Integer leaveType;

    @ApiModelProperty(value = "请假天数,最小0.5天")
    private BigDecimal leaveDays;

    @ApiModelProperty("请假类型描述")
    private String leaveTypeDesc;

    @ApiModelProperty("请假原因")
    private String leaveReason;

    @ApiModelProperty("审批状态：0待审批 1已通过 2已拒绝 3已撤回")
    private Integer approveStatus;

    @ApiModelProperty("审批状态描述")
    private String approveStatusDesc;
}
