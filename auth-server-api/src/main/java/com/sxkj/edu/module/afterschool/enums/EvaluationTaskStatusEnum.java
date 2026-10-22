package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评价任务状态枚举
 */
@Getter
@AllArgsConstructor
public enum EvaluationTaskStatusEnum {
    NOT_STARTED(0, "未开始"),
    IN_PROGRESS(1, "进行中"),
    ENDED(2, "已结束"),
    DISABLED(3, "停用");

    private final Integer code;
    private final String desc;

    public static EvaluationTaskStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EvaluationTaskStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
