package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程类别类型枚举
 */
@Getter
@AllArgsConstructor
public enum CategoryTypeEnum {
    AFTER_SCHOOL_CATEGORY(1, "课后类别"),
    LEARNING_MODULE(2, "学习模块");

    private final Integer code;
    private final String desc;

    public static CategoryTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CategoryTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
