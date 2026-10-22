package com.sxkj.edu.module.system.privilege.domain.dto;

import com.sxkj.edu.common.validation.ChineseAnno;
import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.common.validation.EnglishNumAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class PrivilegeFunctionDTO {

    @ApiModelProperty("功能点名称")
    @NotBlank(message = "功能点名称不能为空")
    @ChineseTitleAnno
    private String functionName;

    @ApiModelProperty("所属菜单Key")
    @NotBlank(message = "所属菜单Key不能为空")
    @EnglishNumAnno
    private String menuKey;

    @ApiModelProperty("功能点Key")
    @NotBlank(message = "功能点Key不能为空")
    @EnglishNumAnno
    private String functionKey;

    @ApiModelProperty("url列表")
    private String url;

    @ApiModelProperty("排序")
    @NotNull(message = "请输入功能点顺序")
    private Integer sort;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty(value = "是否展示 1-是 2-否",required = true)
    @NotNull(message = "请输入是否展示")
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
