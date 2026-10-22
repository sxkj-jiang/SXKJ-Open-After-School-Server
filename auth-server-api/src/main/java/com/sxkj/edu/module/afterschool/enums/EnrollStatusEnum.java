package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报名状态枚举
 */
@Getter
@AllArgsConstructor
public enum EnrollStatusEnum {
    PENDING_CONFIRM(0, "待确认"),
    ENROLLED(1, "已报名"),
    CANCELLED(2, "已取消"),
    ASSIGNED(3, "已分配"),
    UNASSIGNED(4, "未分配");

    private final Integer code;
    private final String desc;

    public static EnrollStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EnrollStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
