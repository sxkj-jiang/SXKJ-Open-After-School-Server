package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalArticleTypeUptDisableAo {

    @ApiModelProperty(value = "主键id",required = true)
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "是否启用 0-否 1-是",required = true)
    @NotNull(message = "是否启用不能为空")
    private Integer isEnable;
}
