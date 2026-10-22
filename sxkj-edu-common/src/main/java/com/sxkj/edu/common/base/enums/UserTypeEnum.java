package com.sxkj.edu.common.base.enums;


import com.sxkj.edu.common.base.domain.BaseEnum;

public enum UserTypeEnum implements BaseEnum {

    /** 用户类型：0-管理员,1-学生(家长),2-教师,3-学校管理员,4-班主任,5-外部教师
     */
    ADMIN(0, "管理员"),
    TEACH(2, "教师"),
    STU(1, "学生(家长)"),
    SCHOOL_ADMIN(3, "学校管理员"),
    LEADER(4, "班主任"),
    AGENCY_TEACH(5, "外部单位教师");

    private Integer value;

    private String desc;

    UserTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return this.desc;
    }
}