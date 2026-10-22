package com.sxkj.edu.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户角色
 *
 * @author jiangjj
 * @date 2017年12月21日上午09:09:31
 */
@Data
public class EmployeeRoleDTO {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String code;


}
