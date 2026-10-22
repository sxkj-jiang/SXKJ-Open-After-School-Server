package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预警状态枚举
 */
@Getter
@AllArgsConstructor
public enum WarningStatusEnum {
    UNHANDLED(0, "未处理"),
    HANDLING(1, "处理中"),
    HANDLED(2, "已处理"),
    CLOSED(3, "已关闭");

    private final Integer code;
    private final String desc;

    public static WarningStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (WarningStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
