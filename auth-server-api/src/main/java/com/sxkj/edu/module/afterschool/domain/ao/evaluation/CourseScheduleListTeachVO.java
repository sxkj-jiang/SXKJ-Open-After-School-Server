package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CourseScheduleListTeachVO {

    @ApiModelProperty(value = "课程班名称")
    private String courseClassName;

    @ApiModelProperty(value = "教师名称")
    private String teacherName;

    @ApiModelProperty(value = "节次序号")
    private Integer sectionNo;

}
