package com.sxkj.edu.common.base.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Description : 审核类型表
 *@Author : JIANGJJ
 *@Date : 2023-2-7 15:11
 *@Company : SXKJ
 *@Version : 1.0
 **/
public enum ExamStatusEnum {
    pass(1, "审核通过"),

    wait(2, "待审核"),
    fail(3, "审核拒绝"),
    back(4, "撤回");

    Integer code;
    String name;

    ExamStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


    private static final Map<Integer, ExamStatusEnum> keyMap = new HashMap<>();

    public static String getValue(Integer code) {
        ExamStatusEnum[] roleEnums = values();
        for (ExamStatusEnum roleEnum : roleEnums) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum.getValue(code);
            }
        }
        return null;
    }

    public static List<Integer> getAllCode() {
        ExamStatusEnum[] roleEnums = values();
        List<Integer> allCode = new ArrayList<>();
        for (ExamStatusEnum roleEnum : roleEnums) {
            allCode.add(roleEnum.getCode());
        }
        return allCode;
    }

    public static ExamStatusEnum fromCode(String code) {
        return keyMap.get(code);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
