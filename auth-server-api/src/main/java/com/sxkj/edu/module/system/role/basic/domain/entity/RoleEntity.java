package com.sxkj.edu.module.system.role.basic.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@TableName("t_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoleEntity extends BaseEntity {

    // 角色编码
    private String code;

    private String roleName;

    private String remark;

    private Integer status;

    // 角色类型：0-管理员，1-校友学生，2-校友教职工，3-学校管理员，4-外部单位(企业管理员等)
    private Integer roleType;

    // 是否是跟学校有关的角色，1-是，0-否
    private Integer isSchool;

    // 排序，从小到大排序
    private Integer sort;
}
