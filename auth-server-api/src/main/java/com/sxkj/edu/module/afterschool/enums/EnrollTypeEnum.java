package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程班报名方式枚举
 */
@Getter
@AllArgsConstructor
public enum EnrollTypeEnum {
    PUBLIC_ENROLL(1, "公开报名"),
    INVITE_ENROLL(2, "邀请报名"),
    CLASS_ARRANGE(3, "行政班安排");

    private final Integer code;
    private final String desc;

    public static EnrollTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EnrollTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
