package com.sxkj.edu.domain.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class LoginCodeCheckAo {


    @ApiModelProperty(value = "图形验证码", required = true)
    @NotBlank(message = "图形验证码不能为空")
    private String code;

    @ApiModelProperty(value = "验证码uuid",required = true)
    @NotBlank(message = "验证码uuid不能为空")
    private String codeUuid;

}
