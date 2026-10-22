package com.sxkj.edu.module.afterschool.enums;

/**
 * 待办来源类型枚举
 */
public enum ToDoTaskBizTypeEnum {
    TEACHER_ADJUST("TEACHER_ADJUST", "教师调课"),
    LOGISTICS_SAFETY_RECORD("LOGISTICS_SAFETY_RECORD", "后勤安全"),
    PROBLEM_REMINDER("PROBLEM_REMINDER", "问题整改"),
    INVITE_COURSE("INVITE_COURSE", "邀请选课"),
    EVALUATE_COURSE("EVALUATE_COURSE", "评价任务"),
    PERSONAL("PERSONAL", "个人创建");

    private final String code;
    private final String desc;

    ToDoTaskBizTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ToDoTaskBizTypeEnum getByCode(String sourceBizType) {
        for (ToDoTaskBizTypeEnum value : ToDoTaskBizTypeEnum.values()) {
            if (value.code.equals(sourceBizType)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
