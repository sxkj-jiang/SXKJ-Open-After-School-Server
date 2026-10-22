package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生去向预警-管理端分页查询返回VO
 *
 * @author yh
 * @date 2026-06-11
 */
@Data
@ApiModel("学生去向预警-管理端分页查询返回VO")
public class AsWarningAdminPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("行政班级名称")
    private String className;

    @ApiModelProperty("学生去向")
    private String destinationName;

    @ApiModelProperty("考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("考勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("发起时间（课次开始时间）")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date triggerTime;

    @ApiModelProperty("预警等级：1正常 2预警 3紧急 ")
    private Integer warningLevel;

    @ApiModelProperty("预警等级描述")
    private String warningLevelDesc;

    @ApiModelProperty("出勤更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date attUpdateTime;
}
