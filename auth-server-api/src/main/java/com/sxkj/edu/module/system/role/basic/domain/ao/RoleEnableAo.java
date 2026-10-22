package com.sxkj.edu.module.system.role.basic.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class RoleEnableAo {

    @ApiModelProperty(value = "角色编号", required = true)
    @NotNull(message = "角色编号不能为空")
    private Long roleId;


    @ApiModelProperty("角色是否启用，1-启用，2，禁用")
    private Integer status;

}
