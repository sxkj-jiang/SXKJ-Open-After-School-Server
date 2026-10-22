package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生课表项VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("学生课表项")
public class AsStudentScheduleItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("第几周（每月第几周）")
    private Integer weekOfMonth;

    @ApiModelProperty("周几（1-7，1表示周一，7表示周日）")
    private Integer weekDay;

    @ApiModelProperty("周几名称（如：周一、周二）")
    private String weekDayName;

    @ApiModelProperty("日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课地点")
    private String placeName;
}
