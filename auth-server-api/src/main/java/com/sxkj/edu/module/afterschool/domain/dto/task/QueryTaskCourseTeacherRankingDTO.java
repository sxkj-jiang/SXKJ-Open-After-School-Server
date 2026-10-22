package com.sxkj.edu.module.afterschool.domain.dto.task;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class QueryTaskCourseTeacherRankingDTO {


    // 课程班id
    private Long id;

    /**
     * 被评价教师姓名
     */
    private String teacherName;

    private String courseClassName;

    // 教师id
    private Long teacherId;

}
