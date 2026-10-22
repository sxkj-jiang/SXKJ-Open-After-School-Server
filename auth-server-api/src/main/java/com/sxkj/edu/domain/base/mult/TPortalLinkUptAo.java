package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalLinkUptAo {

    @ApiModelProperty(value = "主键id,更新时必填")
    private Long id;

    @ApiModelProperty(value = "来源分类")
    private String linkType;

    @ApiModelProperty(value = "链接名称", required = true)
    @NotBlank(message = "链接名称不能为空")
    private String linkName;

    @ApiModelProperty("排序，大到小排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "展示状态,1-启用，其他禁用", required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "链接地址", required = true)
    @NotBlank(message = "链接地址不能为空")
    private String linkUrl;

    @ApiModelProperty(value = "链接说明")
    private String linkDesc;
}
