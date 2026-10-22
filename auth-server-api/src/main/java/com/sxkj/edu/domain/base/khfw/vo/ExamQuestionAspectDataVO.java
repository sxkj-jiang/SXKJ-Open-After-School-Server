package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamQuestionAspectDataVO {

    @ApiModelProperty("维度名称")
    private String aspectName;

    @ApiModelProperty("题目数量")
    private Integer aspectTitleNum;

    @ApiModelProperty("分数")
    private Integer aspectScore;

    @ApiModelProperty("规则的维度占比")
    private Integer weightValue;

    public ExamQuestionAspectDataVO(){
        this.aspectTitleNum = 0;
        this.aspectScore = 0;
    }
}
