package com.sxkj.edu.domain.base.khfw.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionObjectDto {

    @ApiModelProperty("题目")
    private String titleContent;

    @ApiModelProperty("分析")
    private String analyze;

    @ApiModelProperty("选项")
    private List<QuestionItemObjectDto> questionItemObjects;

    @ApiModelProperty("正确答案")
    private String correct;

}
