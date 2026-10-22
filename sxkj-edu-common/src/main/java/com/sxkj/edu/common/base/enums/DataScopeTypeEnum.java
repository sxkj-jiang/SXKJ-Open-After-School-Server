package com.sxkj.edu.common.base.enums;


import com.sxkj.edu.common.base.domain.BaseEnum;

public enum DataScopeTypeEnum implements BaseEnum {

    DEFAULT(0,0,"默认类型","数据范围样例");

    private Integer value;
    private Integer sort;
    private String name;
    private String desc;

    DataScopeTypeEnum(Integer value,Integer sort,String name,String desc) {
        this.value = value;
        this.sort = sort;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public Integer getSort() {
        return sort;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }


}
