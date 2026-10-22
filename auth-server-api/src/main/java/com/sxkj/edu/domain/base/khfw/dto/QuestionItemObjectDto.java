package com.sxkj.edu.domain.base.khfw.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QuestionItemObjectDto {


    @ApiModelProperty(value = "选项",required = true)
    @NotBlank(message = "选项不能为空")
    private String prefix;

    @ApiModelProperty(value = "分值",required = true)
    @NotNull(message = "分值不能为空")
    private Integer score;

    @ApiModelProperty(value = "序号",required = true)
    @NotNull(message = "序号不能为空（题目正常顺序）")
    private Integer sortNo;

}
