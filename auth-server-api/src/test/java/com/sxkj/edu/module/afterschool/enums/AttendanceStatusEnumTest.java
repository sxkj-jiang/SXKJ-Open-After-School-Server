package com.sxkj.edu.module.afterschool.enums;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * 考勤状态业务口径测试。
 */
public class AttendanceStatusEnumTest {

    @Test
    public void presentStatusesShouldContainNormalLateAndEarlyLeaveOnly() {
        assertEquals(Arrays.asList(1, 2, 3), AttendanceStatusEnum.getPresentCodes());
    }
}
