package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalArticleManageShowAo {

    @ApiModelProperty(value = "主键id",required = true)
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "是否展示 0-否 1-是",required = true)
    @NotNull(message = "是否展示不能为空")
    private Integer isShow;

}
