package com.sxkj.edu.module.system.employee.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 恭喜用户状态
 *
 * @author jiangjj
 * @date 2020年12月19日下午2:06:31
 */
@Data
public class UserUpdateStatusAo implements Serializable {

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "用户状态：1-禁用，0-启用", required = true)
    @NotNull(message = "用户状态不能为空")
    private Integer userStatus;


}
