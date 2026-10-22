package com.sxkj.edu.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 登录
 *
 * @author jiangjj
 * @date 2020年12月19日上午11:49:46
 */
@Data
public class EmployeeLoginFormDTO {

    @NotNull(message = "登录名不能为空")
    @ApiModelProperty(example = "长度为6-15个字符", required = true)
    private String loginName;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(example = "123456", required = true)
    private String loginPwd;

    @ApiModelProperty(value = "验证码uuid")
    private String codeUuid;

    @ApiModelProperty(value = "验证码")
    private String code;

}
