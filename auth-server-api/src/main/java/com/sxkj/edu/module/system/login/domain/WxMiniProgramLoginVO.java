package com.sxkj.edu.module.system.login.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信小程序登录响应对象
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("微信小程序登录响应对象")
public class WxMiniProgramLoginVO {

    @ApiModelProperty("访问令牌")
    private String accessToken;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("真实姓名")
    private String actualName;

    @ApiModelProperty("是否首次登录")
    private Boolean isFirstLogin;

    @ApiModelProperty("审批状态：0-待审批 1-已通过 2-已驳回")
    private Integer approveStatus;
}
