package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 整改状态枚举
 */
@Getter
@AllArgsConstructor
public enum RectifyStatusEnum {
    PENDING(0, "待整改"),
    RECTIFYING(1, "整改中"),
    RECTIFIED(2, "已整改"),
    COMPLETED(3, "已完成"),
    OVERDUE(4, "已逾期");

    private final Integer code;
    private final String desc;

    public static RectifyStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RectifyStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
