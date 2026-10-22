package com.sxkj.edu.module.system.employee.domain.dto;

import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description : 用户更新
 * @Author : JIANGJJ
 * @Date : 2025-5-6 15:36
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class EmployeePlatformUpdateDTO {

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String actualName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty(value = "手机号", required = true)
    @Mobile
    private String phone;

    @ApiModelProperty("身份证(可选)")
    private String idCard;

    @ApiModelProperty("生日(可选),格式为：yyyy-mm-dd")
    private String birthday;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty(value = "用户简介")
    private String userDesc;

    @ApiModelProperty(value = "用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师", required = true)
    private Integer userType;

}
