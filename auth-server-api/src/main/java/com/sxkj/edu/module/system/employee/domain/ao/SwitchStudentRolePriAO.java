package com.sxkj.edu.module.system.employee.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class SwitchStudentRolePriAO {

    @ApiModelProperty(value = "学生id,必填",required = true)
    @NotNull(message = "学生id必填")
    private Long studentId;

}
