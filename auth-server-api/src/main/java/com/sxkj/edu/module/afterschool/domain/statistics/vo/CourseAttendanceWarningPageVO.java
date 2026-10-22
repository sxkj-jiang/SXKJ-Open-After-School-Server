package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 出勤预警分页列表VO
 */
@Data
@ApiModel("出勤预警分页列表VO")
public class CourseAttendanceWarningPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("适用年级ID集合JSON（内部使用）")
    private String applicableGradeIds;

    @ApiModelProperty("适用年级名称，多个用分号连接")
    private String gradeNames;

    @ApiModelProperty("应到人数")
    private Integer expectedAttendanceCount;

    @ApiModelProperty("已完成课时")
    private Integer finishedLessonCount;

    @ApiModelProperty("出勤率数值（内部使用）")
    private Double attendanceRateValue;

    @ApiModelProperty("出勤率")
    private String attendanceRate;

    @ApiModelProperty("授课教师名称")
    private String teacherName;
}
