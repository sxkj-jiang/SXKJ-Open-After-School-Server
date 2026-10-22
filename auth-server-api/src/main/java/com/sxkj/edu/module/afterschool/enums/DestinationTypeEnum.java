package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 去向类型枚举
 */
@Getter
@AllArgsConstructor
public enum DestinationTypeEnum {
    COURSE_CLASS(1, "课程班"),
    GO_HOME(2, "回家"),
    TRAINING_INSTITUTION(3, "培训机构"),
    STAY_CLASS(4, "留班"),
    ADMINISTRATIVE_CLASS(5, "行政课"),
    OTHER(6, "其他");

    private final Integer code;
    private final String desc;

    public static DestinationTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DestinationTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
