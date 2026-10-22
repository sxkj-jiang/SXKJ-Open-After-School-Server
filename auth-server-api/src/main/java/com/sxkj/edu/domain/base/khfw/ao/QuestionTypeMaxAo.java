package com.sxkj.edu.domain.base.khfw.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QuestionTypeMaxAo {

    @ApiModelProperty(value = "学科类型",required = true)
    @NotBlank (message = "学科类型不能为空")
    private String subjectType;
}
