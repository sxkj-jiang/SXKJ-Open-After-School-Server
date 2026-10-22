package com.sxkj.edu.module.afterschool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 考勤状态枚举
 */
@Getter
@AllArgsConstructor
public enum AttendanceStatusEnum {
    NORMAL(1, "正常/到课"),
    LATE(2, "迟到"),
    EARLY_LEAVE(3, "早退"),
    ABSENT(4, "缺勤"),
    LEAVE(5, "请假");

    private static final List<Integer> PRESENT_CODES = Collections.unmodifiableList(Arrays.asList(
            NORMAL.getCode(),
            LATE.getCode(),
            EARLY_LEAVE.getCode()
    ));

    private final Integer code;
    private final String desc;

    /**
     * 返回统一的学生出勤状态编码：正常、迟到、早退。
     */
    public static List<Integer> getPresentCodes() {
        return PRESENT_CODES;
    }

    public static AttendanceStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AttendanceStatusEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
