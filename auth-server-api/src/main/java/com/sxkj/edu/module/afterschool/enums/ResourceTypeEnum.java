package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型枚举
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {
    DIRECTORY(1, "目录"),
    DOCUMENT(2, "文档"),
    IMAGE(3, "图片"),
    VIDEO(4, "视频"),
    AUDIO(5, "音频"),
    LINK(6, "链接");

    private final Integer code;
    private final String desc;

    public static ResourceTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResourceTypeEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
