package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批状态枚举
 */
@Getter
@AllArgsConstructor
public enum RegisterStatusEnum {
    PENDING(0, "待注册"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回"),
    APR_ING(3, "待审核");

    private final Integer code;
    private final String desc;

    public static RegisterStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RegisterStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
