package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价指标体系状态枚举
 */
@Getter
@AllArgsConstructor
public enum IndicatorSystemStatusEnum {
    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    private final Integer code;
    private final String desc;

    public static IndicatorSystemStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (IndicatorSystemStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
