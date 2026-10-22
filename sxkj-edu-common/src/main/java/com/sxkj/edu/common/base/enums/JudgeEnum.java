package com.sxkj.edu.common.base.enums;


import com.sxkj.edu.common.base.domain.BaseEnum;

/**
 *@Description : 是与否
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:49
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum JudgeEnum implements BaseEnum {

    NO(0, "否"),
    YES(1, "是");

    private Integer value;
    private String desc;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }

//    public Integer getCode() {
//        return code;
//    }
//
//    public String getValue() {
//        return value;
//    }

    JudgeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


}
