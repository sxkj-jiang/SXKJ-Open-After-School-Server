package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {
    MALE(0, "男"),
    FEMALE(1, "女");

    private final Integer code;
    private final String desc;

    public static GenderEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (GenderEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
