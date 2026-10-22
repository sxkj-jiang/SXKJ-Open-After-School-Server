package com.sxkj.edu.module.system.employee.domain.dto;

import com.sxkj.edu.common.validation.LoginNameAnno;
import com.sxkj.edu.common.validation.Mobile;
import com.sxkj.edu.common.validation.UserName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 添加用户
 *
 * @author lidoudou
 * @date 2017年12月19日下午2:06:31
 */
@Data
public class EmployeeBaseDTO implements Serializable {

    @ApiModelProperty(value = "姓名", required = true)
    @UserName
    private String actualName;

    @ApiModelProperty(value = "登录名", required = true)
    @NotBlank(message = "登录名不能为空")
    @LoginNameAnno
    private String loginName;

    @ApiModelProperty("别名")
    private String nickName;

    @ApiModelProperty("性别,0-男，1-女")
    private Integer sex;

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

    @ApiModelProperty(value = "用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师", required = true)
    private Integer userType;

}
