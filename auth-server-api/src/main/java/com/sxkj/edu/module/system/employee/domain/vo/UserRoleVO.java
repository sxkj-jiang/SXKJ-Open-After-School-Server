package com.sxkj.edu.module.system.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author Administrator
 * @Date 2024/11/25 14:52
 */
@Setter
@Getter
@ToString
public class UserRoleVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户名称")
    private String actualName;

    @ApiModelProperty("岗位名称")
    private String positionName;


    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;

}
