package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 提交状态枚举（用于课堂反馈、后勤安全等）
 */
@Getter
@AllArgsConstructor
public enum SubmitStatusEnum {
    DRAFT(0, "草稿"),
    SUBMITTED(1, "已提交"),
    RETURNED(2, "退回");

    private final Integer code;
    private final String desc;

    public static SubmitStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SubmitStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
