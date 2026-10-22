package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalProtocolUseCaseAo {

    @ApiModelProperty(value = "使用场景 1-用户注册 2-企业认证",required = true)
    @NotNull(message = "使用场景不能为空")
    private Integer useCase;
}
