package com.sxkj.edu.common.base.enums;

import lombok.Getter;

@Getter
public enum EnableEnum {

    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    private final int code;

    private final String description;

    EnableEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescriptionByCode(int code) {
        for (EnableEnum value : values()) {
            if (value.code == code) return value.description;
        }
        return "启用";
    }

    public static Integer getCodeByDescription(String description) {
        for (EnableEnum value : values()) {
            if (value.description.equals(description)) return value.code;
        }
        return 1;
    }

}
