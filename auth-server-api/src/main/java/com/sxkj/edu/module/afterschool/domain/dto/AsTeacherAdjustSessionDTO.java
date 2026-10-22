package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师调课课次明细。
 */
@Data
@ApiModel("教师调课课次明细")
public class AsTeacherAdjustSessionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课次所属学期ID")
    private Long termId;

    @ApiModelProperty("课次所属学期名称")
    private String termName;

    @ApiModelProperty("上课日期")
    private String sessionDate;

    @ApiModelProperty("星期几")
    private Integer weekDay;

    @ApiModelProperty("星期名称")
    private String weekDayName;

    @ApiModelProperty("节次名称")
    private String periodName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("原教师ID")
    private Long originalTeacherId;

    @ApiModelProperty("原教师姓名")
    private String originalTeacherName;

    @ApiModelProperty("代课教师ID")
    private Long substituteTeacherId;

    @ApiModelProperty("代课教师姓名")
    private String substituteTeacherName;

    @ApiModelProperty("原地点")
    private String originalPlace;

    @ApiModelProperty("调整后地点")
    private String adjustPlace;

    @ApiModelProperty("是否为教师原申请课次")
    private Boolean requested = false;

    @ApiModelProperty("管理端是否默认选中")
    private Boolean defaultSelected = false;

    @ApiModelProperty("管理端是否可选择")
    private Boolean selectable = true;

    @ApiModelProperty("不可选择原因")
    private String unavailableReason;
}
