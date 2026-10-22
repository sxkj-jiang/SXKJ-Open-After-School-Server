package com.sxkj.edu.domain.base.khfw.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName RuleToggleStatusAO
 * @Description 规则启用/禁用请求
 * @Author backend-architect
 * @Date 2026-01-12
 * @Version 1.0
 **/
@Data
public class RuleToggleStatusAO {
    @ApiModelProperty(value = "规则ID", required = true)
    @NotBlank(message = "规则ID不能为空")
    private String ruleId;        // 规则ID

    @ApiModelProperty(value = "启用状态（true-启用，false-禁用）", required = true)
    @NotNull(message = "启用状态不能为空")
    private Boolean isEnabled;    // 启用状态
}
