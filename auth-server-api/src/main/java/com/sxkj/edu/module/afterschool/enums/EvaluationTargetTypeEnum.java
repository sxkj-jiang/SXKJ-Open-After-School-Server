package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价对象类型枚举
 */
@Getter
@AllArgsConstructor
public enum EvaluationTargetTypeEnum {
    COURSE_CLASS(1, "课程班"),
    TEACHER(2, "教师"),
    OTHER(3, "其他");

    private final Integer code;
    private final String desc;

    public static EvaluationTargetTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EvaluationTargetTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
