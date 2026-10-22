package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticKeyWordCountVO {

    @ApiModelProperty(value = "统计词")
    private String keyword;

    @ApiModelProperty(value = "数量")
    private Integer count;
}
