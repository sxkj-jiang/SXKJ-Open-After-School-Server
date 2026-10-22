package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionTypeMaxVO {

    @ApiModelProperty("试题类型")
    private String questionType;

    @ApiModelProperty("最大数量")
    private Integer maxNum;

}
