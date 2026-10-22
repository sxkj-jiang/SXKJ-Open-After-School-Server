package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 每周各年级出勤统计VO
 */
@Data
public class WeekGradeAttendanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 周序号
     */
    private Integer weekNum;

    /**
     * 周标签，如"第1周"
     */
    private String weekLabel;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 年级名称
     */
    private String gradeName;

    /**
     * 出勤次数
     */
    private Integer attendanceCount;

    /**
     * 总次数
     */
    private Integer totalCount;
}
