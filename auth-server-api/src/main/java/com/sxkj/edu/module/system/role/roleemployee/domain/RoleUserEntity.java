package com.sxkj.edu.module.system.role.roleemployee.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@TableName("t_role_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoleUserEntity extends BaseEntity {

    private Long roleId;

    private Long userId;
}
