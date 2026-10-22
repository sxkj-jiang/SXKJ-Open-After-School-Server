package com.sxkj.edu.common.base.enums;

import lombok.Getter;

/**
 * @author jiangjj
 * @date 2020/6/22
 */
@Getter
public enum MessageTypeEnum {

    /**
     * 普通、通知类消息，暂时只有一种消息
     */
    NOTICE("NOTICE","普通、通知类消息"),
    SYS_NOTICE("SYS","系统通知"),
    PRIVATE_LETTER("PRI","私信"),
    HEART_BEAT("HEART","心跳"),
    IMAGE("IMAGE","图片"),
    TEXT("TEXT","文本"),
    FILE("FILE","文件");

    ;

    private final String key;
    private final String value;

    MessageTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
