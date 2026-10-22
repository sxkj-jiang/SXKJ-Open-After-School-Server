package com.sxkj.edu.common.base.enums;

/**
 * 云直播状态描述
 */
public enum LiveStreamEnum {

    STREAM_ACT("active","活跃"),
    STREAM_INACT("inactive","非活跃"),
    STREAM_FORBID("forbid","禁播"),
    LIVE_ACTIVE("active","直播中"),
    LIVE_INACTIVE("inactive","非活跃"),
    LIVE_FORBID("forbid","禁播"),
    LIVE_NOT_START("not_start","未开始"),
;
    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    LiveStreamEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        LiveStreamEnum[] liveStreamEnums = values();
        for (LiveStreamEnum liveStreamEnum : liveStreamEnums) {
            if (liveStreamEnum.getCode().equals(code)) {
                return liveStreamEnum.getValue();
            }
        }
        return null;
    }
}
