package com.sxkj.edu.module.system.privilege.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PrivilegeMenuVO {

    @ApiModelProperty("编号")
    private Long id;

    @ApiModelProperty("菜单名")
    private String menuName;

    @ApiModelProperty("菜单Key")
    private String menuKey;

    @ApiModelProperty("菜单父级Key")
    private String parentKey;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("前端路由path")
    private String url;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("是否展示")
    private Integer isShow;

    @ApiModelProperty("meta")
    private String metaOrder;

    @ApiModelProperty("meta活动地址")
    private String metaActivePath;

    @ApiModelProperty("meta标题")
    private String title;

    @ApiModelProperty("前端使用字段")
    private String affixTab;

    @ApiModelProperty("前端路径")
    private String component;

    @ApiModelProperty("前端是否隐藏,1-true,0-false")
    private Integer hideInMenu;

    @ApiModelProperty("前端重定向地址")
    private String redirect;

    @ApiModelProperty("菜单描述")
    private String description;

}
