package com.sxkj.edu.module.system.employee.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.validation.Mobile;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import com.sxkj.edu.module.system.role.roleemployee.domain.RoleUserDataDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户列表DTO
 *
 * @author jiangjj
 * @date 2017年12月21日上午09:09:31
 */
@Data
public class EmployeeDTO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelProperty("别名")
    private String nickName;

    @ApiModelProperty("姓名")
    private String actualName;

    @ApiModelProperty("手机号码")
    @Mobile
    private String phone;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("性别,0-男，1-女")
    private Integer sex;

    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("是否离职")
    private Integer isLeave;

    @ApiModelProperty("是否有效，1-有效，其他无效")
    private Integer isValid;

    @ApiModelProperty("启用禁用，1-启用，0-禁用")
    private Integer status;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("审批状态: 0-待审批 1-已通过 2-已驳回")
    private Integer approveStatus;

    @ApiModelProperty(value = "用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师", required = true)
    private Integer userType;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("岗位名称")
    private String positionName;

    @ApiModelProperty("角色名称合并")
    private String userRoleName;

    @ApiModelProperty("用户拥有的角色列表")
    private List<RoleSelectedVO> userRoleList;

    @ApiModelProperty("用户角色列表")
    private List<RoleUserDataDTO> userRoleCurrList;

    @ApiModelProperty("用户头像")
    private String logoUrl;

    @ApiModelProperty("学校id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("学校代码")
    private String schoolCode;
}
