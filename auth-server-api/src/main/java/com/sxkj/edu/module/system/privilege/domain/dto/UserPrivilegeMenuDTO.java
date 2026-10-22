package com.sxkj.edu.module.system.privilege.domain.dto;

import com.sxkj.edu.common.anno.ApiModelPropertyEnum;
import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.common.validation.EnglishChineseAnno;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class UserPrivilegeMenuDTO {

    @ApiModelProperty(value = "菜单id，更新施必填")
    private Long id;

    @ApiModelPropertyEnum(enumDesc = "菜单类型,1-菜单，2-功能，3-其他", value = PrivilegeTypeEnum.class)
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "菜单名", required = true)
    @NotBlank(message = "菜单名不能为空")
    @ChineseTitleAnno
    private String name;

    @ApiModelProperty(value = "菜单Key", required = true)
    @EnglishChineseAnno
    private String key;

    @ApiModelProperty("父级菜单Key,为根节点时可不传")
    private String parentKey;

    @ApiModelProperty(value = "前端路由path", required = true)
    @NotNull(message = "前端路由path不能为空")
    private String path;

    @ApiModelProperty(value = "排序字段", required = true)
    @NotNull(message = "菜单项顺序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "是否展示 1-是 2-否", required = true)
    @NotNull(message = "是否展示不能为空")
    private Integer isShow;

    @ApiModelProperty("前端路径")
    private String component;

    @ApiModelProperty("前端meta")
    private UserMenuMetaDto meta;

    @ApiModelProperty("前端重定向地址")
    private String redirect;

    @ApiModelProperty("菜单描述")
    private String description;
}
