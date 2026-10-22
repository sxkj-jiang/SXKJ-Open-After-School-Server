package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待办状态枚举
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    TODO(0, "待办"),
    COMPLETED(1, "已完成"),
    CANCELLED(2, "已取消");

    private final Integer code;
    private final String desc;

    public static TaskStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TaskStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
