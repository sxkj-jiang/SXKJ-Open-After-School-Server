package com.sxkj.edu.module.support.codegenerator.constant;



/**
 *@Description : sql操作符 gt,lt 目前只支持Date
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:59
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum SqlOperateTypeEnum{


    LIKE(1, "like"),
    EQUALS(2, "equals"),
    IN(3, "in");

    private Integer type;

    private String name;

    SqlOperateTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
