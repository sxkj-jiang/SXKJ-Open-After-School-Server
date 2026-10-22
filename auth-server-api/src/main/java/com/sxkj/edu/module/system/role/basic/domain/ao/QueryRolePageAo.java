package com.sxkj.edu.module.system.role.basic.domain.ao;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class QueryRolePageAo extends PageParamDTO {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色代码")
    private String roleCode;

    @ApiModelProperty("角色是否启用，1-启用，2，禁用")
    private Integer status;

    @ApiModelProperty("角色编码集合")
    private List<Long> roleIdList;

}
