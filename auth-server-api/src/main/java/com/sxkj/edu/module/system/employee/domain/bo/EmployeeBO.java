package com.sxkj.edu.module.system.employee.domain.bo;

import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import lombok.Getter;

import java.util.Set;


@Getter
public class EmployeeBO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 员工名称
     */
    private String actualName;

    /**
     * 别名
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师
     */
    private Integer userType;

    /**
     * 是否离职
     */
    private Integer isLeave;

    /**
     * 是否有效 1-有效 其他无效
     */
    private Integer isValid;

    /**
     * 校友学校id
     */
    private Long schoolId;

    /**
     * 是否为超级管理员
     */
    private Boolean isSuperman;

    /**
     * 角色集
     */
    private Set<Long> roleSet;

    public EmployeeBO(TBaseUserEntity employeeEntity, boolean isSuperman,Set<Long> roleSet) {
        this.id = employeeEntity.getId();
        this.loginName = employeeEntity.getLoginName();
        this.actualName = employeeEntity.getActualName();
        this.nickName = employeeEntity.getNickName();
        this.phone = employeeEntity.getPhone();
        this.departmentId = employeeEntity.getDepartmentId();
        this.isLeave = employeeEntity.getIsLeave();
        this.isValid = employeeEntity.getIsValid();
        this.userType = employeeEntity.getUserType();
        this.idCard = employeeEntity.getIdCard();
        this.isSuperman = isSuperman;
        this.schoolId = employeeEntity.getSchoolId();
        this.roleSet = roleSet;
    }
}
