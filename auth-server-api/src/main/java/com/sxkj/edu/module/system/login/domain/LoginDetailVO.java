package com.sxkj.edu.module.system.login.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import com.sxkj.edu.module.system.role.roleprivilege.domain.dto.RolePrivilegeSimpleDTO;
import com.sxkj.edu.module.system.role.roleprivilege.domain.dto.UserRolePrivilegeVbenDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 登录返回DTO
 *
 * @author jiangjj
 * @date 2017年12月21日上午09:06:31
 */
@Data
public class LoginDetailVO {

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

    @ApiModelProperty("用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师")
    private Integer userType;

    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty("创建者id")
    private Long createUser;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("是否离职")
    private Integer isLeave;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("登陆token")
    private String xAccessToken;

    @ApiModelProperty("是否为超管")
    private Boolean isSuperMan;

    @ApiModelProperty("角色列表")
    private List<RoleSelectedVO> roleEntityList;

    @ApiModelProperty("姓名")
    private String linkName;

    @ApiModelProperty("用户头像地址")
    private String logoUrl;

    @ApiModelProperty("性别:0-男-1-女")
    private String sex;

    @ApiModelProperty("校友角色学校信息")
    private LoginStuSchoolDetailVO schoolInfo;

    @ApiModelProperty("用户权限")
    private List<UserRolePrivilegeVbenDto> privilegeList;

    @ApiModelProperty("用户审批状态：0-待审批 1-已通过 2-已驳回")
    private Integer approveStatus;

    @ApiModelProperty("家长小孩信息")
    private List<LoginStuParentDetailVO> childInfo;

}
