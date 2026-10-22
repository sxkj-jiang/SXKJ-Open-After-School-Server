package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class CourseClassRankingAnswerTeacherLeadDTO {

    // 已评价人数
    private Long evaluatorCount;

    // 平均分
    private Double averageScore;

}
