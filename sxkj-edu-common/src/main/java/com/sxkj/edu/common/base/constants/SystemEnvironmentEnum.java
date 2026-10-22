package com.sxkj.edu.common.base.constants;


import com.sxkj.edu.common.base.domain.BaseEnum;

/**
 *@Description :系统环境枚举类
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:55
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum SystemEnvironmentEnum implements BaseEnum {


    /**
     * dev
     */
    DEV("dev", "开发环境"),

    /**
     * sit
     */
    SIT("sit", "测试环境"),

    /**
     * pre
     */
    PRE("pre", "预发布环境"),

    /**
     * prod
     */
    PROD("prod", "生产环境");


    public static final String DEV_ENV = "dev";


    private String value;

    private String desc;

    SystemEnvironmentEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取定义枚举value值
     *
     * @return Integer
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }

}
