package com.sxkj.edu.domain.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel(value = "BaseTemplateQueryAO对象", description = "基础管理-模板管理")
public class BaseTemplateChildQueryAO extends PageQuery {


    @ApiModelProperty(value = "应用菜单key",required = true)
    @NotBlank(message = "应用菜单编码不能为空")
    private String menu;

    @ApiModelProperty(value = "应用菜单key子编码")
    private String menuChild;

    @ApiModelProperty(value = "创建时间-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建时间-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

}