package com.sxkj.edu.domain.base.khfw.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExamQuestionDetailAo {

    @ApiModelProperty(value = "主键id",required = true)
    @NotNull(message = "主键id不能为空")
    private Long id;

    @ApiModelProperty(value = "题干关键词")
    private String keyword;
}
