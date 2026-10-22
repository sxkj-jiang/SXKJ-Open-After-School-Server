package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoursAttendanceStatisticsVO {

    @ApiModelProperty(value = "出勤率")
    private String attendanceRate;

    @ApiModelProperty(value = "总课程数")
    private Integer totalCourseNum;

    @ApiModelProperty(value = "预警课程数")
    private Integer warningCourseNum;

    @ApiModelProperty(value = "出勤趋势列表")
    private List<AttendanceTrendStatisticsVO> attendanceTrendList;
}
