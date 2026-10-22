package com.sxkj.edu.module.system.privilege.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class UserMenuDto implements Serializable {
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


    private String path;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 父级key
     */
    private String parentKey;

    private Integer isShow;

    private String menuSource;

    // meta对象
    private UserMenuMetaDto meta;
    private String component;
    private String redirect;

    // 子级对象
    private List<UserMenuDto> children;


}
