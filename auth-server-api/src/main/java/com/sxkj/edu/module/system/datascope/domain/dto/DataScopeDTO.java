package com.sxkj.edu.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DataScopeDTO {

    @ApiModelProperty("数据范围类型")
    private Integer dataScopeType;

    @ApiModelProperty("数据范围名称")
    private String dataScopeTypeName;

    @ApiModelProperty("描述")
    private String dataScopeTypeDesc;

    @ApiModelProperty("顺序")
    private Integer dataScopeTypeSort;

}
