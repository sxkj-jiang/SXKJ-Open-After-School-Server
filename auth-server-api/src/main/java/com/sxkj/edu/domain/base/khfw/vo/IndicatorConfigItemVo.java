package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndicatorConfigItemVo {

    @ApiModelProperty(value = "维度名称")
    private String name;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "描述")
    private String description;
}
