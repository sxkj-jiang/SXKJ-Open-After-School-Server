package com.sxkj.edu.module.afterschool.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家长选课记录平铺VO（SQL查询原始结果，一行对应一条课次记录）
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsParentEnrollmentFlatVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 学期ID */
    private Long termId;

    /** 学生ID */
    private Long studentId;

    /** 学期名称 */
    private String termName;

    /** 课程班ID */
    private Long courseClassId;

    /** 课程班名称 */
    private String courseClassName;

    /** 星期几 */
    private Integer weekDay;

    /** 开始时间（HH:mm） */
    private String startTime;

    /** 结束时间（HH:mm） */
    private String endTime;

    /** 上课地点ID */
    private String placeId;

    /** 上课地点名称 */
    private String placeName;

    /** 课程类型名称 */
    private String categoryName;

    /** 报名方式 */
    private Integer enrollType;

    /** 学生选课/生效时间 */
    private Date studentEnrollTime;

    /** 开课周期开始时间 */
    private Date classStartTime;

    /** 开课周期结束时间 */
    private Date classEndTime;
}
