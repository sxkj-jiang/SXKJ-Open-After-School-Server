package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价指标适用角色枚举
 */
@Getter
@AllArgsConstructor
public enum EvaluationApplicableRoleEnum {
    STUDENT(1, "家长"),
    TEACHER(2, "教师"),
    ADMIN(3, "管理员");

    private final Integer code;
    private final String desc;

    public static EvaluationApplicableRoleEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EvaluationApplicableRoleEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
