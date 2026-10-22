package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CourseScheduleListDTO {

    @ApiModelProperty(value = "课次id")
    private Long sessionId;

    @ApiModelProperty(value = "课程班id")
    private Long courseClassId;

    @ApiModelProperty(value = "课程班名称")
    private String courseClassName;

    @ApiModelProperty(value = "教师名称")
    private String teacherName;

    @ApiModelProperty(value = "星期几(1-7)")
    private Integer weekDay;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty("第几节 序号")
    private Integer sectionNo;

    @ApiModelProperty("节次名称")
    private String sectionName;

    @ApiModelProperty(value = "行政班安排适用班级ID集合，JSON数组")
    private String applicableClassIds;

    @ApiModelProperty(value = "适用年级ID集合，JSON数组")
    private String applicableGradeIds;

    @ApiModelProperty(value = "报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;
}
