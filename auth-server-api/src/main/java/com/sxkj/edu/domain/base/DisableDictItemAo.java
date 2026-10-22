package com.sxkj.edu.domain.base;

import com.sxkj.edu.common.validation.UserName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DisableDictItemAo {

    @ApiModelProperty(value = "字典id,必填",required = true)
    @NotNull(message = "字典id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(1:正常;0:禁用)",required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;
}
