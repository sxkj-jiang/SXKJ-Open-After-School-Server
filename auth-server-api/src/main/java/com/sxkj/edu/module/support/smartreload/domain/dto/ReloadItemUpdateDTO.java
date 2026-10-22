package com.sxkj.edu.module.support.smartreload.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReloadItemUpdateDTO {

    @ApiModelProperty(value = "标签", required = true)
    @NotBlank(message = "标签不能为空")
    private String tag;

    @ApiModelProperty(value = "状态标识", required = true)
    @NotBlank(message = "状态标识不能为空")
    private String identification;

    @ApiModelProperty("reload时传入的参数，可以为空")
    private String args;
}
