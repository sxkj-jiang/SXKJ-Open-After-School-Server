package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentProfileDetailCourseVO {

    @ApiModelProperty("课程ID")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("完成度")
    private String courseCompletion;

    @ApiModelProperty("学期名称")
    private String termName;
}
