package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 巡课计划状态枚举
 */
@Getter
@AllArgsConstructor
public enum PlanStatusEnum {
    DRAFT(0, "草稿"),
    SUBMITTED(1, "已提交"),
    IN_PROGRESS(2, "进行中"),
    COMPLETED(3, "已完成");

    private final Integer code;
    private final String desc;

    public static PlanStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PlanStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
