package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("教师任课学生信息")
public class AsTeacherCourseStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("年级班级名称")
    private String gradeClassName;
}
