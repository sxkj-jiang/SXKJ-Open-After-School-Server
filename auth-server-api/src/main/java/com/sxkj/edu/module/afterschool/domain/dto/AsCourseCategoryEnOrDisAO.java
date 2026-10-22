package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AsCourseCategoryEnOrDisAO {

    @ApiModelProperty(value = "类别id",required = true)
    @NotNull(message = "类别id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(1启用 0停用)",required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer enabledStatus;
}
