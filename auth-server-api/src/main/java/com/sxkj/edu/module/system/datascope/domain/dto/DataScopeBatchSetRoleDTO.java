package com.sxkj.edu.module.system.datascope.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class DataScopeBatchSetRoleDTO {

    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @ApiModelProperty("设置信息")
    @Valid
    private List<DataScopeBatchSetDTO> batchSetList;
}
