package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 班主任查询自然班级学生去向查询请求参数
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("班主任查询自然班级学生去向查询请求参数")
public class AsHeadTeacherStudentDestinationPageQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;


    @ApiModelProperty(value = "班主任教师ID")
    private Long headTeacherId;

    @ApiModelProperty(value = "学期ID")
    private Long termId;

    @ApiModelProperty(value = "学生姓名（模糊查询）")
    private String studentName;

    @ApiModelProperty(value = "自然班级ID")
    private Long classId;
}
