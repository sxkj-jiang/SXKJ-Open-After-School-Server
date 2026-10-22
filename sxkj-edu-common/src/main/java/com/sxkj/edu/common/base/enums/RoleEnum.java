package com.sxkj.edu.common.base.enums;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *@Description : 角色枚举类
 *@Author : JIANGJJ
 *@Date : 2022-12-29 15:23
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum RoleEnum {

    PORTAL_ROLE_STU(2L,"portal_role_stu", "学生(家长)"),
    PORTAL_ROLE_TEACHER(3L,"portal_role_teacher", "学校内任课教师"),
    PORTAL_ROLE_TEACH_LEADER(5L,"portal_role_teacher_leader", "班主任"),
    PORTAL_ROLE_SCHOOL_ADMIN(4L,"portal_school_admin", "学校管理员(教务处)"),
    PORTAL_ROLE_OTHER(6L,"portal_other_teacher", "外部机构教师"),
    PORTAL_ROLE_ADMIN(1L,"portal_role_admin", "超级管理员");

    /**
     * roleId
     */
    private Long roleId;

    private String code;

    private String value;

    RoleEnum(Long roleId, String code, String value) {
        this.roleId = roleId;
        this.code = code;
        this.value = value;
    }

    /**
     * admin - 管理类设定的角色，all - 全部系统设定的角色
     *
     * @param type
     * @return
     */
    public static List<String> roleFilter(String type) {
        if ("admin".equals(type)) {
            return getAdminCode();
        }
        return getAllCode();
    }

    public static String getValue(String code) {
        RoleEnum[] roleEnums = values();
        for (RoleEnum roleEnum : roleEnums) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum.getValue();
            }
        }
        return null;
    }

    public static List<String> getAllCode() {
        RoleEnum[] roleEnums = values();
        List<String> allCode = new ArrayList<>();
        for (RoleEnum roleEnum : roleEnums) {
            allCode.add(roleEnum.getCode());
        }
        return allCode;
    }

    /**
     * 不能授权的角色列表
     *
     * @return
     */
    public static List<String> getAdminCode() {
        List<String> allCode = new ArrayList<>();
        allCode.add("portal_role_ptgly_teach");
        allCode.add("portal_role_admin");
        return allCode;
    }

    /**
     * 判断是否管理员
     *
     * @param roleId
     * @return
     */
    public static boolean isAdmin(String roleId) {
        return RoleEnum.PORTAL_ROLE_ADMIN.getCode().equals(roleId);
    }

    /**
     * 判断是否管理员
     *
     * @param roleIds
     * @return
     */
    public static boolean isAdmin(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        return roleIds.contains(RoleEnum.PORTAL_ROLE_ADMIN.getCode());
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public Long getRoleId() {
        return roleId;
    }
}
