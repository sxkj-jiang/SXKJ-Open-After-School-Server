package com.sxkj.edu.module.afterschool.domain.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 课次重建迁移快照：按课程班保存需迁移的学生报名和旧未开始课次ID。
 */
public class RebuildEnrollmentSnapshot {
    public final Map<Long, List<EnrollmentSessionSnapshot>> enrollmentsByCourse;
    public final Map<Long, List<Long>> oldSessionIdsByCourse;

    public RebuildEnrollmentSnapshot(Map<Long, List<EnrollmentSessionSnapshot>> enrollmentsByCourse,
                                     Map<Long, List<Long>> oldSessionIdsByCourse) {
        this.enrollmentsByCourse = enrollmentsByCourse;
        this.oldSessionIdsByCourse = oldSessionIdsByCourse;
    }

    public static RebuildEnrollmentSnapshot empty() {
        return new RebuildEnrollmentSnapshot(Collections.emptyMap(), Collections.emptyMap());
    }
}
