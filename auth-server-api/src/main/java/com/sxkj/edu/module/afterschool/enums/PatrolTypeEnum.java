package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 巡课类型枚举
 */
@Getter
@AllArgsConstructor
public enum PatrolTypeEnum {
    SPECIAL_PATROL(1, "专项巡课"),
    DAILY_PATROL(2, "日常巡课");

    private final Integer code;
    private final String desc;

    public static PatrolTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PatrolTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
