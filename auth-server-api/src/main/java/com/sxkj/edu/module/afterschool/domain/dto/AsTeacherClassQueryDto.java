package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class AsTeacherClassQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师id")
    private Long teacherId;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "班级id")
    private Long classId;

    @ApiModelProperty(value = "班级名称")
    private String className;
}
