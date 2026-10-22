package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 学科启用/禁用参数
 *
 * @author system
 * @date 2026-08-26
 */
@Data
public class AsSubjectEnOrDisAO {

    @ApiModelProperty(value = "学科ID", required = true)
    @NotNull(message = "学科ID不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(1启用 0停用)", required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer enabled;
}
