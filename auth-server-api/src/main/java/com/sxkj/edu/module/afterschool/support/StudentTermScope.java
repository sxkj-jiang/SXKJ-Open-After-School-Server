package com.sxkj.edu.module.afterschool.support;

import lombok.Getter;

import java.io.Serializable;

/**
 * 学生业务学期范围。
 *
 * <p>课程班和课次的学期统一沿课后服务向上追溯，禁止使用页面默认学期覆盖。</p>
 */
@Getter
public class StudentTermScope implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long schoolId;
    private final Long termId;
    private final Long serviceId;
    private final Long courseClassId;
    private final Long sessionId;

    /**
     * 创建学生业务学期范围。
     */
    public StudentTermScope(Long schoolId, Long termId, Long serviceId,
                            Long courseClassId, Long sessionId) {
        this.schoolId = schoolId;
        this.termId = termId;
        this.serviceId = serviceId;
        this.courseClassId = courseClassId;
        this.sessionId = sessionId;
    }
}
