package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalInstructionsQueryDetAo {

    @ApiModelProperty(value = "操作指南编号",required = true)
    @NotNull(message = "操作指南编号不能为空")
    private Long id;



}
