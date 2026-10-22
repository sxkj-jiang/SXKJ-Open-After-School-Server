package com.sxkj.edu.module.afterschool.domain.dto;

import java.util.Date;

/**
 * 开课周期日期二元组（已通过格式与逻辑校验）。
 */
public class EnrollCycleDate {
    public final Date courseStart;
    public final Date courseEnd;

    public EnrollCycleDate(Date courseStart, Date courseEnd) {
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
    }
}
