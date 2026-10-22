package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排课管理列表 Tab 展示状态。
 */
@Getter
@AllArgsConstructor
public enum AsServiceDisplayTabEnum {

    UNPUBLISHED(0, "未发布"),
    PENDING_ENROLL(1, "待报名"),
    ENROLLING(2, "报名中"),
    PENDING_COURSE(3, "待开课"),
    RUNNING(4, "进行中"),
    FINISHED(5, "已结束");

    private final Integer code;
    private final String desc;

    public static AsServiceDisplayTabEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AsServiceDisplayTabEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
