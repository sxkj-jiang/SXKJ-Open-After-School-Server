package com.sxkj.edu.module.system.login.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信小程序手机号登录请求对象
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("微信小程序手机号登录请求对象")
public class WxMiniProgramPhoneLoginAO {

    @ApiModelProperty(value = "微信登录凭证code", required = true)
    @NotBlank(message = "微信登录凭证code不能为空")
    private String code;

    @ApiModelProperty(value = "手机号获取凭证code", required = true)
    @NotBlank(message = "手机号获取凭证code不能为空")
    private String phoneCode;

    @ApiModelProperty("用户类型：1-教师 2-学生(家长)")
    private Integer userType;
}
