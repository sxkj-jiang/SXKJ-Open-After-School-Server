package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课次状态枚举
 */
@Getter
@AllArgsConstructor
public enum SessionStatusEnum {
    NOT_STARTED(0, "未开始"),
    IN_PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成"),
    CANCELLED(3, "取消");

    private final Integer code;
    private final String desc;

    public static SessionStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SessionStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
