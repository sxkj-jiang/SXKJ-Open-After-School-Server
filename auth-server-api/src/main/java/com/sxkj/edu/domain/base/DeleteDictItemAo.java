package com.sxkj.edu.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteDictItemAo {

    @ApiModelProperty(value = "字典id,必填",required = true)
    @NotNull(message = "字典id不能为空")
    private Long id;

}
