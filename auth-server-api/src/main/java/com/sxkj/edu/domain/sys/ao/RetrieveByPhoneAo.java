package com.sxkj.edu.domain.sys.ao;

import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "RetrieveByPhoneAo", description = "手机找回密码")
public class RetrieveByPhoneAo {

    @ApiModelProperty(value = "手机号", required = true)
    @Mobile
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "登录密码,需要用AES加密", required = true)
    @NotBlank(message = "登录密码不能为空")
    private String loginPwd;

    @ApiModelProperty(value = "验证码", required = true)
    @NotNull(message = "验证码不能为空")
    private String code;
}
