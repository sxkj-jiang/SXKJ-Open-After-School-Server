package com.sxkj.edu.domain.base.khfw.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionTypeNumDto {

    @ApiModelProperty(value = "题目类型 1-单选题 2-多选题 3-文本题")
    private Integer questionType;

    @ApiModelProperty("题目数量")
    private Integer questionNum;
}
