package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价题目类型枚举
 */
@Getter
@AllArgsConstructor
public enum QuestionTypeEnum {
    SINGLE_CHOICE(1, "单选"),
    MULTIPLE_CHOICE(2, "多选"),
    FILL_BLANK(3, "文本");

    private final Integer code;
    private final String desc;

    public static QuestionTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (QuestionTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
