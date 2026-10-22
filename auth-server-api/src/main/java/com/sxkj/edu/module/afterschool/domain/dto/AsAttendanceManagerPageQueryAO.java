package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 管理端分页查询学生考勤记录参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("管理端分页查询学生考勤记录参数")
public class AsAttendanceManagerPageQueryAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("课程班班级ID")
    private Long classId;

    @ApiModelProperty("课程活动名称")
    private String serviceName;

    @ApiModelProperty("考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假")
    private Integer attendanceStatus;

    @ApiModelProperty("上课时间开始,格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sessionStartTime;

    @ApiModelProperty("上课时间结束,格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sessionEndTime;

    @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("学期ID")
    private Long termId;
}
