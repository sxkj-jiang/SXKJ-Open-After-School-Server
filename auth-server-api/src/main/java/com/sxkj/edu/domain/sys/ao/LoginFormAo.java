package com.sxkj.edu.domain.sys.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
public class LoginFormAo {

    @ApiModelProperty(value = "用户名称/电话号码", required = true)
    @NotBlank(message = "用户名称/电话号码 不能为空")
    private String keyWord;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "来源 0-教务端 1-平台网页端 2-移动端", required = true)
    @NotNull(message = "来源不能为空")
    private Integer from;

}
