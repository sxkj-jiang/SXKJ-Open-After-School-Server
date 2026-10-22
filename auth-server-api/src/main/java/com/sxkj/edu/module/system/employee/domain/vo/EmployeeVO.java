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
public class EmployeeVO {

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

    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("单位名称")
    private String workUnitName;

    @ApiModelProperty("性别,0-男，1-女")
    private Integer sex;

    @ApiModelProperty("是否离职")
    private Integer isLeave;

    @ApiModelProperty("启用禁用，1-启用，0-禁用")
    private Integer status;

    @ApiModelProperty("是否有效，1-有效，其他无效")
    private Integer isValid;

    @ApiModelProperty("审批状态: 0-待审批 1-已通过 2-已驳回")
    private Integer approveStatus;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("邮箱")
    private String email;

//    @ApiModelProperty("岗位关联信息")
//    private List<PositionRelationResultDTO> positionRelationList;

    @ApiModelProperty("岗位名称")
    private String positionName;

    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;

    @ApiModelProperty("角色名称合并,多个用英文逗号分割")
    private String userRoleName;

    @ApiModelProperty("用户角色列表")
    private List<RoleUserDataDTO> userRoleList;

    @ApiModelProperty("用户头像")
    private String logoUrl;

    @ApiModelProperty("学校id,非超管账户都有值")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;
}
