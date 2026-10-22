package com.sxkj.edu.module.system.role.basic.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description : 移除用户
 * @Author : JIANGJJ
 * @Date : 2025-5-9 17:20
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class RemoveRoleBatchAo {

    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    protected Long roleId;

    /**
     * 员工id集合
     */
    @ApiModelProperty(value = "员工id", required = true)
    @NotNull(message = "员工id不能为空")
    protected Long userId;

}
