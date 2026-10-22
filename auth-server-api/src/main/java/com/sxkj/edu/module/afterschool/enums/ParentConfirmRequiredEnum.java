package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 家长确认要求枚举
 */
@Getter
@AllArgsConstructor
public enum ParentConfirmRequiredEnum {
    NOT_REQUIRED(0, "不需要"),
    REQUIRED(1, "需要");

    private final Integer code;
    private final String desc;

    public static ParentConfirmRequiredEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ParentConfirmRequiredEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
