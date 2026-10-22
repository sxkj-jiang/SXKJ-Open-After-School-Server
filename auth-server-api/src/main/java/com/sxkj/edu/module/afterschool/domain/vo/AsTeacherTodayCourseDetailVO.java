package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师今日课程详情VO
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@ApiModel("教师今日课程详情VO")
public class AsTeacherTodayCourseDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("年级")
    private String gradeName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("课堂状态：0未开始 1进行中 2已结束")
    private Integer classStatus;

    @ApiModelProperty("课堂状态描述")
    private String classStatusDesc;

    @ApiModelProperty("开课日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("课程介绍")
    private String intro;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("报名方式名称")
    private String enrollTypeName;

    @ApiModelProperty("课程类别名称")
    private String categoryName;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("年级ID")
    private Long gradeId;
}
