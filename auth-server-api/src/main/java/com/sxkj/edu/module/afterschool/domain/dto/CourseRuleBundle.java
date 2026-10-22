package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;

import java.util.Collections;
import java.util.List;

public class CourseRuleBundle {
    public final AsCourseClassEntity course;
    public final List<AsCourseScheduleRuleAO> rules;

    public CourseRuleBundle(AsCourseClassEntity course, List<AsCourseScheduleRuleAO> rules) {
        this.course = course;
        this.rules = rules == null ? Collections.emptyList() : rules;
    }
}
