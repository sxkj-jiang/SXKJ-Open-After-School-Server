package com.sxkj.edu.module.system.privilege.domain.dto;

import com.sxkj.edu.common.anno.ApiModelPropertyEnum;
import com.sxkj.edu.common.validation.ChineseAnno;
import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.common.validation.EnglishChineseAnno;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class PrivilegeMenuDTO {

    @ApiModelProperty(value = "菜单id，更新施必填")
    private Long id;

    @ApiModelPropertyEnum(enumDesc = "菜单类型", value = PrivilegeTypeEnum.class)
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "菜单名", required = true)
    @NotBlank(message = "菜单名不能为空")
    @ChineseTitleAnno
    private String menuName;

    @ApiModelProperty(value = "菜单Key", required = true)
    @EnglishChineseAnno
    private String menuKey;

    @ApiModelProperty("父级菜单Key,为根节点时可不传")
    private String parentKey;

    @ApiModelProperty(value = "前端路由path", required = true)
    @NotNull(message = "前端路由path不能为空")
    private String url;

    @ApiModelProperty(value = "排序字段", required = true)
    @NotNull(message = "菜单项顺序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否展示 1-是 2-否", required = true)
    @NotNull(message = "是否展示不能为空")
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
}
