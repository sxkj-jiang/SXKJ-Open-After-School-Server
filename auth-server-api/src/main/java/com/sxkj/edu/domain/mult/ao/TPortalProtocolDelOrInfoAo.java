package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalProtocolDelOrInfoAo {

    @ApiModelProperty(value = "协议主键id",required = true)
    @NotNull(message = "协议主键id不能为空")
    private Long id;

}
