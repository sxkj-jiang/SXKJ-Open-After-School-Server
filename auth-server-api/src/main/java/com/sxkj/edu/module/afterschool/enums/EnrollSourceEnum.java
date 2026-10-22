package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报名来源枚举
 */
@Getter
@AllArgsConstructor
public enum EnrollSourceEnum {
    PUBLIC_ENROLL(1, "家长公开报名"),
    TEACHER_INVITE(2, "教师邀请"),
    CLASS_ARRANGE(3, "行政班安排");

    private final Integer code;
    private final String desc;

    public static EnrollSourceEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EnrollSourceEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
