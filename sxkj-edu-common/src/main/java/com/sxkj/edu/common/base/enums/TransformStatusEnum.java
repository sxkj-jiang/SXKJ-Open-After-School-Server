package com.sxkj.edu.common.base.enums;


import com.sxkj.edu.common.base.domain.BaseEnum;

/**
 * 课程转码状态、用于aliyun_video的状态标注
 */
public enum TransformStatusEnum implements BaseEnum {

    UN_TRANSFORM("UnTransform", "无需转码"),
    FAIL("Fail", "转码失败"),
    PROCESSING("Processing", "转码中"),
    SUCCESS("Success", "已转码");

    private String value;
    private String desc;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    TransformStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
