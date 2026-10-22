package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别要求枚举
 */
@Getter
@AllArgsConstructor
public enum GenderRequirementEnum {
    NO_LIMIT(0, "不限"),
    MALE_ONLY(1, "男"),
    FEMALE_ONLY(2, "女");

    private final Integer code;
    private final String desc;

    public static GenderRequirementEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (GenderRequirementEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
