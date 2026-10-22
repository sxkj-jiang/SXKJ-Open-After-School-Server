package com.sxkj.edu.domain.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 用户角色
 * @Author : jjj$
 * @Date : $ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Slf4j
@Data
public class QueryUserRoleDto implements Serializable  {

    @ApiModelProperty("用户ID")
    private Long Id;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("角色代码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

}
