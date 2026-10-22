package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 管理端分页查询教师考勤记录参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("管理端分页查询教师考勤记录参数")
public class AsAttendanceManagerTeacherQueryAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("教师姓名/手机号")
    private String teacherName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程活动名称")
    private String serviceName;

    @ApiModelProperty("考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假")
    private Integer attendanceStatus;

    @ApiModelProperty("上课时间开始，格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sessionStartTime;

    @ApiModelProperty("上课时间结束，格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sessionEndTime;
}
