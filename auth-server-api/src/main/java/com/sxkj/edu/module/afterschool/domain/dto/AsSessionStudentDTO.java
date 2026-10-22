package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课次学生信息DTO（学生名称、课程班名称、作息时间段）
 */
@Data
public class AsSessionStudentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生名称
     */
    private String studentName;

    /**
     * 课程班名称
     */
    private String courseClassName;

    /**
     * 课次开始时间（来自t_as_schedule_period）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    /**
     * 课次结束时间（来自t_as_schedule_period）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    /**
     * 老师用户ID
     */
    private Long teacherUserId;
}
