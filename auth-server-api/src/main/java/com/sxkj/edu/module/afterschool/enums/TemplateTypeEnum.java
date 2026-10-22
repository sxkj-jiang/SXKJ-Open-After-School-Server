package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 作息方案类型枚举
 */
@Getter
@AllArgsConstructor
public enum TemplateTypeEnum {
    SCHEDULE_MANAGEMENT(1, "作息管理"),
    AFTER_SCHOOL_TIME(2, "课后时间管理");

    private final Integer code;
    private final String desc;

    public static TemplateTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TemplateTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
