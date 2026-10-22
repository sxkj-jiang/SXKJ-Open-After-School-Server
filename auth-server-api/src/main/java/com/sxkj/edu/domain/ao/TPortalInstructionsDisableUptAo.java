package com.sxkj.edu.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalInstructionsDisableUptAo {

    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "操作指南状态，1-已发布(启用)，2-待发布(禁用)", required = true)
    @NotNull(message = "操作指南状态不为空")
    private Integer operStatus;


}
