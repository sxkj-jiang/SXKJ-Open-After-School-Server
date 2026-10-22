package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsSchedulePeriodEntity;

import java.util.Collections;
import java.util.List;

/**
 * 导入行转换结果：课程班实体 + 解析出的上课星期和命中的课后时间段，用于后续生成排课规则。
 */
public class ImportCourseClassRow {
    public final AsCourseClassEntity entity;
    public final List<Integer> weekDays;
    public final List<AsSchedulePeriodEntity> periods;

    public ImportCourseClassRow(AsCourseClassEntity entity, List<Integer> weekDays, List<AsSchedulePeriodEntity> periods) {
        this.entity = entity;
        this.weekDays = weekDays;
        this.periods = periods == null ? Collections.emptyList() : periods;
    }
}
