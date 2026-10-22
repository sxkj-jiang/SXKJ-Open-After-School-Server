package com.sxkj.edu.domain.sys.ao;

import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class SmsCheckAo {

    @ApiModelProperty(value = "手机号码", required = true)
    @NotNull(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @ApiModelProperty(value = "验证码", required = true)
    @NotNull(message = "验证码不能为空")
    private String code;

}
