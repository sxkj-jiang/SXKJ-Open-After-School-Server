package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "BaseTemplateQueryAO对象", description = "基础管理-模板管理")
public class BaseTemplateQueryDetailAO {

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "应用菜单key",required = true)
    @NotBlank(message = "应用菜单key不能为空")
    private String menu;


}