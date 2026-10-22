package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批状态枚举
 */
@Getter
@AllArgsConstructor
public enum ApproveStatusEnum {
    PENDING(0, "待注册审批"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回"),
    WITHDRAWN(3, "已撤回");

    private final Integer code;
    private final String desc;

    public static ApproveStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ApproveStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
