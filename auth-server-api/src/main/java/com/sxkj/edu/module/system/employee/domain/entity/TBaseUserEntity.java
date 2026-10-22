package com.sxkj.edu.module.system.employee.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础用户表
 *
 * @author jiangjj
 * @date 2017年12月19日下午1:34:48
 */
@Data
@TableName("t_base_user")
public class TBaseUserEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8794328598524272806L;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPwd;

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
     * 性别,0-男，1-女
     */
    private Integer sex;
    /**
     * 座机
     */
    private String mobile;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 出生日期
     */
    private String birthday;


    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 单位名称
     */
    private String workUnitName;

    /**
     * 是否离职
     */
    private Integer isLeave;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批状态: 0-待审批 1-已通过 2-已驳回
     */
    private Integer approveStatus;


    // 用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师
    private Integer userType;

    // 学校id,非超管账户都有值
    private Long schoolId;

    // 用户状态启用禁用，1-启用，0-禁用
    private Integer status;

    /**
     * 用户头像地址
     */
    private String logoUrl;
}
