package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 巡课记录状态枚举
 */
@Getter
@AllArgsConstructor
public enum PatrolStatusEnum {
    PENDING(0, "待巡查"),
    IN_PROGRESS(1, "进行中"),
    SUBMITTED(2, "已提交"),
    OVERDUE(3, "逾期");

    private final Integer code;
    private final String desc;

    public static PatrolStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PatrolStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
