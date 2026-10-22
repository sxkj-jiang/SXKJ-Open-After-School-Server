package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用审核记录日志、枚举
 */
@Getter
@AllArgsConstructor
public enum BaseExamRecordEnum {
    KCB_TRANSFER("KCB_TRANSFER", "课程班移交");

    private final String code;
    private final String desc;

    public static BaseExamRecordEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (BaseExamRecordEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
