package com.sxkj.edu.module.system.privilege.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("t_privilege_info")
public class UserMenuEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3848408566432912414L;

    /**
     * 功能权限类型：1.页面 2.功能点
     */
    private Integer type;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由name 英文关键字
     */
    @TableField(value = "`key`")
    private String key;


    private String url;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 父级key
     */
    private String parentKey;

    private String icon;

    private Integer isShow;

    private String menuSource;

    private Integer metaOrder;
    private String metaActivePath;
    private String metaTitle;
    private String component;
    private Integer hideInMenu;
    private String redirect;

    /**
     * 菜单描述
     */
    private String description;


}
