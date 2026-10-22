package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生去向预警-去处理-学生列表项视图对象
 */
@Data
@ApiModel("学生去向预警-去处理-学生列表项")
public class AsWarningHandleStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("年级+班级")
    private String gradeClassName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("今日去向")
    private String destinationName;

    @ApiModelProperty("出勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("学生去向预警ID")
    private Long warningRecordId;

    @ApiModelProperty("学生出勤记录ID")
    private Long attendanceRecordId;

    @ApiModelProperty("异常原因")
    private String warningReason;

    @ApiModelProperty("预警状态 0未处理 1处理中 2已处理 3已关闭")
    private Integer warningStatus;
}
