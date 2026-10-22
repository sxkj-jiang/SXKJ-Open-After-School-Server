package com.sxkj.edu.domain.sys.dto;

import com.sxkj.edu.common.validation.LoginNameAnno;
import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "RegisterDTO",description = "注册")
public class RegisterDTO {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @LoginNameAnno
    private String loginName;

    @ApiModelProperty(value = "手机号", required = true)
    @Mobile
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "短信验证码", required = true)
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

    @ApiModelProperty(value = "性别", required = true)
    @NotBlank(message = "性别不能为空")
    private String sex;

    @ApiModelProperty(value = "固定电话")
    private String mobile;

    @ApiModelProperty(value = "登录密码", required = true)
    @NotBlank(message = "登录密码不能为空")
    private String loginPwd;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String pwd;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    private String actualName;

    @ApiModelProperty(value = "身份证", required = true)
    // @NotBlank(message = "身份证不能为空")
    private String idCard;

    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "图形验证码的uuid", required = true)
    private String uuid;

    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;


}
