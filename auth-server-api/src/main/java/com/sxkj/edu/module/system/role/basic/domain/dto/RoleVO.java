package com.sxkj.edu.module.system.role.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RoleVO {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色类型，0-管理员，1-教师，2-学生,默认为2")
    private Integer roleType;

    @ApiModelProperty("角色编码")
    private String code;

    @ApiModelProperty("角色备注")
    private String remark;

    @ApiModelProperty("角色状态，2-禁用，1-启用")
    private Integer status;
}
