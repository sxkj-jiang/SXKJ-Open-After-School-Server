package com.sxkj.edu.domain.sys.dto;

import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "RetrieveDTO",description = "找回密码")
public class RetrieveDTO {

    @ApiModelProperty(value = "手机号", required = true)
    @Mobile
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "登录密码", required = true)
    @NotBlank(message = "登录密码不能为空")
    private String loginPwd;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String pwd;
}
