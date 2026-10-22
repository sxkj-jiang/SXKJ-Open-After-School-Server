package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程班维度评价排名DTO（Mapper查询结果映射）
 * 用于applicableRole为3（教师）和5（班主任）时，按课程班分组聚合评价数据
 *
 * @author system
 * @date 2026-07-07
 */
@Data
public class CourseClassRankingDTO {

    /**
     * 课程班ID
     */
    private Long courseClassId;

    /**
     * 课程班名称
     */
    private String courseClassName;

    /**
     * 授课教师ID（t_as_course_class.teacher_id）
     */
    private Long teacherId;

    /**
     * 授课教师姓名
     */
    private String teacherName;

    /**
     * 已评价人数（按被评价学生去重，target_student_id不为空且submit_status=1）
     */
    private Long evaluatedCount;

    /**
     * 所有已提交答案的平均分
     */
    private BigDecimal averageScore;

    /**
     * 课程班报名总人数（从t_as_enrollment统计）
     */
    private Long totalEnrolled;
}
