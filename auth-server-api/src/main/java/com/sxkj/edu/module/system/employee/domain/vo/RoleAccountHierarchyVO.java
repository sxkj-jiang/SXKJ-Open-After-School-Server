package com.sxkj.edu.module.system.employee.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色账号层级结构VO
 * 一级为角色信息，二级为该角色下的账号列表
 *
 * @author system
 * @date 2026-05-28
 */
@Data
@ApiModel("角色账号层级结构VO")
public class RoleAccountHierarchyVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("该角色下的账号列表")
    private List<AccountInfoVO> accounts;
}
