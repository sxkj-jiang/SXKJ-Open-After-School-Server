package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 教师维度评价排名DTO（Mapper查询结果映射）
 * 用于applicableRole为2（学生家长）和4（学校管理员）时，按被评价教师分组聚合评价数据
 *
 * @author system
 * @date 2026-07-07
 */
@Data
public class TeacherRankingDTO {

    // 课程班id
    private Long courseClassId;

    /**
     * 被评价教师ID（target_teacher_id）
     */
    private Long targetTeacherId;

    /**
     * 被评价教师姓名
     */
    private String teacherName;

    /**
     * 评价人数（按评价人去重）
     */
    private Long evaluatorCount;

    /**
     * 平均分
     */
    private BigDecimal averageScore;
}
