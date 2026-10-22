package com.sxkj.edu.common.base.enums;


/**
 *@Description : 短信类型
 *@Author : JIANGJJ
 *@Date : 2022-12-29 15:20
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum SmsTypeEnum {

    REG("1", "注册类"),
    MONITORING("2", "监控类"),
    AUDIT("3", "审核类");

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    SmsTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        SmsTypeEnum[] wayBillEnums = values();
        for (SmsTypeEnum wayBillEnum : wayBillEnums) {
            if (wayBillEnum.getCode().equals(code)) {
                return wayBillEnum.getValue();
            }
        }
        return null;
    }
}
