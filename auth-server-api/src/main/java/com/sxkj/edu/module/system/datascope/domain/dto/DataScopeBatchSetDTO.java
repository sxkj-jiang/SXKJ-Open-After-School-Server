package com.sxkj.edu.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class DataScopeBatchSetDTO {

    @ApiModelProperty(value = "数据范围类型", required = true)
    @NotNull(message = "数据范围类型不能为空")
    private Integer dataScopeType;

    @ApiModelProperty(value = "可见范围", required = true)
    @NotNull(message = "可见范围不能为空")
    private Integer viewType;
}
