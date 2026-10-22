package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家长查询选课记录-课程维度VO（学期内的课程）
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsParentEnrollmentCourseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课时间（星期+起止时间拼接）")
    private String classTime;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("开课周期开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date classStartTime;

    @ApiModelProperty("开课周期结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date classEndTime;

    @ApiModelProperty("学生选课/生效时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date studentEnrollTime;

    @ApiModelProperty("开课周期拼接时间，格式为：2023-09-01 - 2023-09-05")
    private String courseDate;

    @ApiModelProperty("课程类型")
    private String categoryName;

    @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("报名方式描述")
    private String enrollTypeDesc;
}
