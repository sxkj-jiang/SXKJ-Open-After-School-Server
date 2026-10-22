package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 考勤对象枚举
 */
@Getter
@AllArgsConstructor
public enum TargetTypeEnum {
    STUDENT(1, "学生"),
    TEACHER(2, "教师");

    private final Integer code;
    private final String desc;

    public static TargetTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TargetTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
