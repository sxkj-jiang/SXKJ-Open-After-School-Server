package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("请假申请视图对象")
public class AsLeaveRequestQueryApplyRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("点到记录ID")
    private Long recordId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("考勤状态 1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("学生去向预警ID")
    private Long warningRecordId;

    @ApiModelProperty("预警状态 0未处理 1处理中 2已处理 3已关闭")
    private Integer warningStatus;

    @ApiModelProperty("异常原因描述")
    private String exceptionDesc;



}
