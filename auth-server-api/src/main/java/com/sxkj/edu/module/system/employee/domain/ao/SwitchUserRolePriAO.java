package com.sxkj.edu.module.system.employee.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class SwitchUserRolePriAO {

    @ApiModelProperty(value = "用户手机号,必填",required = true)
    @NotBlank(message = "用户手机号必填")
    private String phone;

    @ApiModelProperty(value = "用户姓名",required = true)
    @NotBlank(message = "用户姓名必填")
    private String actualName;

    @ApiModelProperty(value = "角色id，切换角色是必填",required = true)
    private Long roleId;

    @ApiModelProperty(value = "用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师,切换身份时必填")
    private Integer userType;

    public SwitchUserRolePriAO() {}

    public SwitchUserRolePriAO(String phone, String actualName, Long roleId, Integer userType) {
        this.phone = phone;
        this.actualName = actualName;
        this.roleId = roleId;
        this.userType = userType;
    }
}
