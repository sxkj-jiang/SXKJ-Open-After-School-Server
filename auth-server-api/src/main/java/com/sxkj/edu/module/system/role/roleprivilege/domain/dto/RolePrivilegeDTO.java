package com.sxkj.edu.module.system.role.roleprivilege.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RolePrivilegeDTO {

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /**
     * 功能权限id 集合
     */
    @ApiModelProperty(value = "功能权限Key集合", required = true)
    @NotNull(message = "功能权限集合不能为空")
    private List<String> privilegeKeyList;

}
