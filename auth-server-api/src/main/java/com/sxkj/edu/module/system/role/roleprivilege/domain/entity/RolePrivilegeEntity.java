package com.sxkj.edu.module.system.role.roleprivilege.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.util.Date;


@Data
@TableName("t_role_privilege")
public class RolePrivilegeEntity extends BaseEntity {

    /**
     * 角色 id
     */
    private Long roleId;

    /**
     * 功能权限 id
     */
    private String privilegeKey;

    public RolePrivilegeEntity() {
    }

    public RolePrivilegeEntity(Long roleId, String privilegeKey) {
        this.roleId = roleId;
        this.privilegeKey = privilegeKey;
        this.setCreateTime(new Date());
        this.setCreateUser(1L);
        this.setIsValid(1);
    }
}
