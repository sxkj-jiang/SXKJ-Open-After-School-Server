package com.sxkj.edu.module.system.role.basic.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RoleSelectedVO extends RoleVO {

    @ApiModelProperty("是否选中")
    private Boolean selected;

    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;
}
