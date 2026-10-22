package com.sxkj.edu.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录返回DTO
 *
 * @author jiangjj
 * @date 2017年12月21日上午09:06:31
 */
@Data
public class LoginGrobInfoVO {

    @ApiModelProperty("访问来源:JWPC-教务端，PTPC-平台端，XCX-小程序")
    private String loginType;

    @ApiModelProperty("用户类型，0-管理员，1-校友学生，2-校友教职工，3-学校管理员，4-外部单位(企业管理员等)")
    private Integer userType;

    @ApiModelProperty("设备名称")
    private String device;


}
