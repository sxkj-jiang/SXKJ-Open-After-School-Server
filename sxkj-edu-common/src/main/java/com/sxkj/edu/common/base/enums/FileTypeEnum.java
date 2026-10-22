package com.sxkj.edu.common.base.enums;


/**
 * 文件类型枚举
 **/
public enum FileTypeEnum {

    DOCUMENT_TYPE("DOCUMENT", "文档类"),
    PICTURE_TYPE("PICTURE", "图片类"),
    SUBJECT_VIDEO_TYPE("SUBJECT_VIDEO", "直播回放"),
    VIDEO_TYPE("VIDEO", "视频类");

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    FileTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        FileTypeEnum[] wayBillEnums = values();
        for (FileTypeEnum wayBillEnum : wayBillEnums) {
            if (wayBillEnum.getCode().equals(code)) {
                return wayBillEnum.getValue();
            }
        }
        return null;
    }
}
