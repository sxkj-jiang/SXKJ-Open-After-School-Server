package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("学生家长审核请求参数")
public class AsStudentStatusQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校id", required = true)
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long termId;

}
