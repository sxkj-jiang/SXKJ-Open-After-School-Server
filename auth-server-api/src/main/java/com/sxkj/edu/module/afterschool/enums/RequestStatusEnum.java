package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调课状态枚举
 */
@Getter
@AllArgsConstructor
public enum RequestStatusEnum {
    PENDING(0, "待处理/待审核"),
    APPROVED(1, "已同意/已通过"),
    REJECTED(2, "已拒绝/已驳回"),
    WITHDRAWN(3, "已撤回");

    private final Integer code;
    private final String desc;

    public static RequestStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RequestStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
