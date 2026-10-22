package com.sxkj.edu.domain.sys.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class LoginPhoneAo {

    @ApiModelProperty(value = "电话号码", required = true)
    @NotNull(message = "电话号码 不能为空")
    private String mobile;

    @ApiModelProperty(value = "验证码", required = true)
    private String code;

}
