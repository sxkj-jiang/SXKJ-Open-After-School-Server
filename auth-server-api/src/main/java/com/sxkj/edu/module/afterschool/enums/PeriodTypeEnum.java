package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间段类型枚举
 */
@Getter
@AllArgsConstructor
public enum PeriodTypeEnum {
    NORMAL_PERIOD(1, "普通节次"),
    AFTER_SCHOOL_PERIOD(2, "课后时间段");

    private final Integer code;
    private final String desc;

    public static PeriodTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PeriodTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
