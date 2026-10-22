package com.sxkj.edu.module.system.privilege.constant;


import com.sxkj.edu.common.base.domain.BaseEnum;

import java.util.Arrays;
import java.util.Optional;

public enum PrivilegeTypeEnum implements BaseEnum {


    MENU(1, "菜单"),

    POINTS(2, "功能点");

    private Integer value;

    private String desc;

    PrivilegeTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PrivilegeTypeEnum selectByValue(Integer value) {
        Optional<PrivilegeTypeEnum> first = Arrays.stream(PrivilegeTypeEnum.values()).filter(e -> e.getValue().equals(value)).findFirst();
        return !first.isPresent() ? null : first.get();
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
