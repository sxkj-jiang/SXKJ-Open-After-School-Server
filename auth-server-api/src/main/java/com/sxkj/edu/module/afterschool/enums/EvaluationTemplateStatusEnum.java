package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价模板状态枚举
 */
@Getter
@AllArgsConstructor
public enum EvaluationTemplateStatusEnum {
    DISABLE(0, "停用"),
    ENABLE(1, "启用");

    private final Integer code;
    private final String desc;

    public static EvaluationTemplateStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EvaluationTemplateStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
