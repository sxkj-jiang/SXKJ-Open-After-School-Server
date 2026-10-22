package com.sxkj.edu.module.system.employee.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号信息VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
@ApiModel("账号信息VO")
public class AccountInfoVO {

    @ApiModelProperty("用户账号ID")
    private Long userId;

    @ApiModelProperty("用户账号名称(actual_name)")
    private String actualName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户账号(login_name)")
    private String loginName;
}
