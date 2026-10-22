package com.sxkj.edu.common.base.enums;

/**
 *@Description : 登录枚举类
 *@Author : JIANGJJ
 *@Date : 2022-12-29 15:23
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum LoginTypeEnum {
    USER_PC_TYPE("PTPC", "平台网页端"),
    USER_WECHAT_TYPE("XCX", "平台小程序"),
    MANAGER_TYPE("JWPC", "管理端");



    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    LoginTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        LoginTypeEnum[] enums = values();
        for (LoginTypeEnum em : enums) {
            if (em.getCode().equals(code)) {
                return em.getValue();
            }
        }
        return null;
    }
}
