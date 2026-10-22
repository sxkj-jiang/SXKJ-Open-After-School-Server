package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请假申请人角色枚举
 */
@Getter
@AllArgsConstructor
public enum ApplicantRoleEnum {
    GUARDIAN(1, "家长"),
    HEAD_TEACHER(2, "班主任"),
    ADMIN(3, "管理端");

    private final Integer code;
    private final String desc;

    public static ApplicantRoleEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ApplicantRoleEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
