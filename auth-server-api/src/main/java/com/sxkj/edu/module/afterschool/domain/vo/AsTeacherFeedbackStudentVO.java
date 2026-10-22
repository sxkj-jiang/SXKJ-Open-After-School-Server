package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师课程反馈-课次学生信息VO
 */
@Data
@ApiModel("教师课程反馈-课次学生信息")
public class AsTeacherFeedbackStudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;
}
