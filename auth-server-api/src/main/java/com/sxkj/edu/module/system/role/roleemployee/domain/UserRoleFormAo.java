package com.sxkj.edu.module.system.role.roleemployee.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class UserRoleFormAo {

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "关联角色id集合")
    private List<Long> roleIds;

    public UserRoleFormAo() {
    }

    public UserRoleFormAo(Long userId, List<Long> roleIds) {
        this.userId = userId;
        this.roleIds = roleIds;
    }
}
