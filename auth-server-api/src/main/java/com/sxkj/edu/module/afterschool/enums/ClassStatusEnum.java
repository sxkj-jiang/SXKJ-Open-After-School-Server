package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程班状态枚举
 */
@Getter
@AllArgsConstructor
public enum ClassStatusEnum {
    NOT_STARTED(1, "未开始"),
    IN_PROGRESS(2, "进行中"),
    FINISHED(3, "已结束"),
    DISABLED(4, "停用");

    private final Integer code;
    private final String desc;

    public static ClassStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ClassStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
