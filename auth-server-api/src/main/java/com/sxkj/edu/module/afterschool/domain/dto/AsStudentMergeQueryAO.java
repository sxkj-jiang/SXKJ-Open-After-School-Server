package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AsStudentMergeQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID",required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty(value = "学校",required = true)
    @NotNull(message = "学校不能为空")
    private Long schoolId;

}
