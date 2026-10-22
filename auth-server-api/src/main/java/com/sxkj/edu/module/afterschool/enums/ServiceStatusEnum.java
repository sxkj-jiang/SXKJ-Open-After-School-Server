package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课后服务状态枚举
 */
@Getter
@AllArgsConstructor
public enum ServiceStatusEnum {
    UNPUBLISHED(0, "未发布"),
    ENROLLING(1, "报名中"),
    IN_PROGRESS(2, "进行中"),
    FINISHED(3, "已结束");

    private final Integer code;
    private final String desc;

    public static ServiceStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ServiceStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
