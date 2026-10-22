package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请假类型枚举
 */
@Getter
@AllArgsConstructor
public enum LeaveTypeEnum {
    SICK_LEAVE(1, "病假"),
    PERSONAL_LEAVE(2, "事假"),
    OTHER(3, "其他");

    private final Integer code;
    private final String desc;

    public static LeaveTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LeaveTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
