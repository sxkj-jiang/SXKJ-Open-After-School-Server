package com.sxkj.edu.module.system.employee.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.validation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 添加用户
 *
 * @author jiangjj
 * @date 2020年12月19日下午2:06:31
 */
@Data
public class EmployeeAddDTO implements Serializable {

    @ApiModelProperty(value = "姓名", required = true)
    @UserName
    private String actualName;

    @ApiModelProperty(value = "登录名", required = true)
    @NotNull(message = "登录名不能为空")
    @LoginNameAnno
    private String loginName;

    @ApiModelProperty("别名")
    private String nickName;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("性别,0-男，1-女")
    private Integer sex;

    @ApiModelProperty("单位名称")
    private String workUnitName;

    @ApiModelProperty(value = "手机号", required = true)
    @Mobile
    private String phone;

    @ApiModelProperty("身份证(可选)")
    @PersonCard
    private String idCard;

    @ApiModelProperty("生日(可选),格式：yyyy-mm-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty(value = "密码，需要传AES加密后的密码值", required = true)
    @NotNull
    @Size(min = 8, max = 64)
    private String loginPwd;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("职位ID 集合")
    private List<Long> positionIdList;

    @ApiModelProperty(value = "用户角色集合，注意只能选择同类角色类型，不能同时选择学生和教师", required = true)
    @NotNull(message = "用户角色集合不为空")
    private List<Long> roleIds;

    @ApiModelProperty("学校id,非超管账户都有值")
    private Long schoolId;

    @ApiModelProperty(value = "用户简介")
    private String userDesc;

    @ApiModelProperty(value = "用户头像")
    private String userLogo;

//    @ApiModelProperty(value = "用户类型：管理员-0,教师-1,学生-2,财务-3,4-院系领导,5-外部单位，默认为1")
//    private Integer userType;

}
