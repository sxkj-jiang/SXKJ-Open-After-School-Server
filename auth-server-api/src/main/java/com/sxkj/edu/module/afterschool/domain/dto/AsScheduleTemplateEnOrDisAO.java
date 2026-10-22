package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 作息管理方案启用/禁用参数
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("作息方案启用/禁用参数")
public class AsScheduleTemplateEnOrDisAO {

    @ApiModelProperty(value = "方案ID", required = true)
    @NotNull(message = "方案ID不能为空")
    private Long id;

    @ApiModelProperty(value = "启用状态：1启用 0停用", required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer enabledStatus;
}
