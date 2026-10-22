package com.sxkj.edu.module.system.datascope.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;


/**
 * @Description : 数据范围与角色关系
 * @Author : JIANGJJ
 * @Date : 2022-9-7 15:03
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
@TableName("t_role_data_scope")
public class DataScopeRoleEntity extends BaseEntity {

    /**
     * 数据范围id
     */
    private Integer dataScopeType;
    /**
     * 数据范围类型
     */
    private Integer viewType;
    /**
     * 角色id
     */
    private Long roleId;
}
