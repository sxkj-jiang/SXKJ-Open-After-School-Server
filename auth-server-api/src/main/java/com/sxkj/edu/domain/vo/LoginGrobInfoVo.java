package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录返回DTO
 *
 * @author jiangjj
 * @date 2017年12月21日上午09:06:31
 */
@Data
public class LoginGrobInfoVo {

    @ApiModelProperty("访问来源:LOGIN_TYPE_MANAGER-管理端，LOGIN_TYPE_PC-平台网页端，LOGIN_TYPE_H5-小程序")
    private String loginType;

    @ApiModelProperty("用户类型，1-学生、2-老师、3-管理员")
    private Integer userType;

    @ApiModelProperty("设备名称")
    private String device;


}
