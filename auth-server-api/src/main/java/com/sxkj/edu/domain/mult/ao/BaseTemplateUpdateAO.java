package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "BaseTemplateUpdateAO对象", description = "基础管理-模板管理")
public class BaseTemplateUpdateAO {

    @ApiModelProperty(value = "ID",required = true)
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "模板名称",required = true)
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    @ApiModelProperty(value = "应用菜单key,对应菜单管理的key",required = true)
    @NotBlank(message = "应用菜单key不能为空")
    private String menu;

    @ApiModelProperty(value = "应用菜单子页面的key，当页面子页面模板时必传")
    private String menuChildCode;

    @ApiModelProperty(value = "启用状态：1-启用 0-禁用")
    private Integer enable;

    @ApiModelProperty(value = "文件编号",required = true)
    @NotNull(message = "文件不能为空")
    private Long attachment;

}