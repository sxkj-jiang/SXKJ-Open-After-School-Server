package com.sxkj.edu.common.base.enums;

/**
 *@Description : 业务类型枚举类
 *@Author : JIANGJJ
 *@Date : 2022-12-29 15:23
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum BusinessTypeEnum {

    TYPE_REGISTER("register", "注册类"),
    TYPE_APPROVAL("approval", "审核类"),
    TYPE_DEVOPS("devops", "运维类");



    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    BusinessTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        BusinessTypeEnum[] enums = values();
        for (BusinessTypeEnum em : enums) {
            if (em.getCode().equals(code)) {
                return em.getValue();
            }
        }
        return null;
    }
}
