package com.sxkj.edu.common.base.enums;


import com.sxkj.edu.common.base.domain.BaseEnum;

public enum DataScopeViewTypeEnum implements BaseEnum {

    ME(0,0,"本人"),

    DEPARTMENT(1,5,"本部门"),

    DEPARTMENT_AND_SUB(2,10,"本部门及下属子部门"),

    ALL(3,15,"全部");

    private Integer value;
    private Integer level;
    private String desc;

    DataScopeViewTypeEnum(Integer value,Integer level, String desc) {
        this.value = value;
        this.level = level;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String getDesc() {
        return desc;
    }


}
