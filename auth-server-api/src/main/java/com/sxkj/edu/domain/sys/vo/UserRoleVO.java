package com.sxkj.edu.domain.sys.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@Description : 用户角色
 *@Author : JIANGJJ
 *@Date : 2022-12-29 16:14
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class UserRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("账号")
    private String userAccount;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("手机号")
    private String userPhone;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;
}
