package com.sxkj.edu.module.afterschool.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生在学期内已占用的课后时段轻量数据。
 *
 * @author system
 * @date 2026-08-12
 */
@Data
public class AsEnrollmentOccupiedSlotData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 学校编号。 */
    private Long schoolId;

    /** 学期编号。 */
    private Long termId;

    /** 学生编号。 */
    private Long studentId;

    /** 课次编号。 */
    private Long sessionId;

    /** 上课日期。 */
    private Date sessionDate;

    /** 星期。 */
    private Integer weekDay;

    /** 开始时间。 */
    private Date startTime;

    /** 结束时间。 */
    private Date endTime;
}
