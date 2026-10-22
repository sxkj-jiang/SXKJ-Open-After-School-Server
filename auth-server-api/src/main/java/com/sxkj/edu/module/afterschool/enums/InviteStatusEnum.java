package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邀请状态枚举
 */
@Getter
@AllArgsConstructor
public enum InviteStatusEnum {
    VALID(1, "有效"),
    INVALID(2, "失效");

    private final Integer code;
    private final String desc;

    public static InviteStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (InviteStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
