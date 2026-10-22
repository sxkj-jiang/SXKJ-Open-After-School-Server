package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价模板类型枚举
 */
@Getter
@AllArgsConstructor
public enum EvaluationTemplateTypeEnum {
    COURSE_CLASS(1, "课程班评价"),
    PATROL(2, "巡课评价"),
    OTHER(3, "其他");

    private final Integer code;
    private final String desc;

    public static EvaluationTemplateTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EvaluationTemplateTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
