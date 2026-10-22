package com.sxkj.edu.module.system.role.basic.domain.dto;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RoleQueryDTO extends PageParamDTO {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("查询条件(用户名称或电话)")
    private String queryCriteria;
}
