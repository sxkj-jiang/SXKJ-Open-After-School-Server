package com.sxkj.edu.module.system.employee.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserDataDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jiangjj
 * @Description: 用户信息
 * @date 2021/8/28 9:04
 */

@Data
public class EmployeeSingVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("别名")
    private String nickName;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("部门id")
    private Long departmentId;


    @ApiModelProperty("用户头像")
    private String logoUrl;

    @ApiModelProperty("学校id,非超管账户都有值")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;
}
