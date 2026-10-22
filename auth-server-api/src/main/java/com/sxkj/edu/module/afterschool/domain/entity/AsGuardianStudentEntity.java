package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 家长学生监护关系实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_guardian_student")
public class AsGuardianStudentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 监护人用户ID
     */
    private Long guardianUserId;

    /**
     * 学生跨学期稳定身份编号。
     */
    private Long studentIdentityId;

    /**
     * 关系名称：妈妈/爸爸等
     */
    private String relationName;

    /**
     * 家长姓名
     */
    private String parentName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 绑定状态：1有效 2解绑
     */
    private Integer bindStatus;

    /**
     * 填写的所有手机号
     */
    private String phoneInfo;
}
