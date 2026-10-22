package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理端教师考勤记录VO
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("管理端教师考勤记录VO")
public class AsAttendanceManagerTeacherVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("考勤id")
    private Long id;

    @ApiModelProperty("姓名")
    private String teacherName;

    @ApiModelProperty("工号")
    private String teacherNo;

    @ApiModelProperty("考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假")
    private Integer attendanceStatus;

    @ApiModelProperty("考勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程活动名称")
    private String serviceName;

    @ApiModelProperty("授课教师")
    private String courseTeacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("上课时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date sessionTime;

    @ApiModelProperty("上课时间格式：14:00-15:00")
    private String classTime;

    @ApiModelProperty("上课时间格式：2026-04-01 14:00-15:00")
    private String classResTime;

    @ApiModelProperty("选课方式：1-公开报名，2-邀请报名，3-行政班安排")
    private Integer enrollType;

    @ApiModelProperty("选课方式描述")
    private String enrollTypeDesc;

    @ApiModelProperty("备注")
    private String remark;
}
