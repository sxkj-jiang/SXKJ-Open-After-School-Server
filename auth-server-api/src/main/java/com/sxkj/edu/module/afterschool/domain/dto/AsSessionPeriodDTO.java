package com.sxkj.edu.module.afterschool.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课次作息时间段DTO（课次ID + 作息节次开始/结束时间）
 */
@Data
public class AsSessionPeriodDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 课次ID（t_as_course_session.id）
     */
    private Long sessionId;

    /**
     * 作息节次开始时间
     */
    private Date startTime;

    /**
     * 作息节次结束时间
     */
    private Date endTime;
}
