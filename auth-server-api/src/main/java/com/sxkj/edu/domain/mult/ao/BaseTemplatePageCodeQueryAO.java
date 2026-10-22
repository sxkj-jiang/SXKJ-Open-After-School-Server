package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 模板管理-根据页面code查询导入模板列表入参
 */
@Data
@ApiModel(value = "BaseTemplatePageCodeQueryAO对象", description = "模板管理-根据页面code查询")
public class BaseTemplatePageCodeQueryAO {

    @ApiModelProperty(value = "页面code,应用菜单key或其父级前缀(右模糊匹配,如courseScheduling可查出courseScheduling_open等)", required = true)
    @NotBlank(message = "页面code不能为空")
    private String pageCode;

    @ApiModelProperty(value = "菜单子页面key")
    private String menuChildCode;
}
