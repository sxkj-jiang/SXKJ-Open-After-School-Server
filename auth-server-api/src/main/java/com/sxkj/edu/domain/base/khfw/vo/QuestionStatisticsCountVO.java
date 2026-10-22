package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionStatisticsCountVO {

    @ApiModelProperty("统计词")
    private String keyword;

    @ApiModelProperty("统计数量")
    private Integer count;
}
