package com.sxkj.edu.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class DataScopeSelectVO {

    @ApiModelProperty("数据范围id")
    private Integer dataScopeType;

    @ApiModelProperty("可见范围")
    private Integer viewType;
}
