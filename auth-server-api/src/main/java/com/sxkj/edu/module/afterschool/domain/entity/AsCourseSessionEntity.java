package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课后服务课次实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_course_session")
public class AsCourseSessionEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 课程班ID
     */
    private Long courseClassId;

    /**
     * 上课日期
     */
    private Date sessionDate;

    /**
     * 星期几
     */
    private Integer weekDay;

    /**
     * 来源作息/课后时间段ID
     */
    private Long schedulePeriodId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 调课前原教师ID
     */
    private Long originalTeacherId;

    /**
     * 生效调课申请ID
     */
    private Long adjustRequestId;

    /**
     * 调课状态：0未调课 1已调课
     */
    private Integer adjustStatus;

    /**
     * 地点名称
     */
    private String placeName;

    /**
     * 课次状态：0未开始 1进行中 2已完成 3取消
     */
    private Integer sessionStatus;

    /**
     * 是否操作考勤点到 0-否 1-是
     */
    private Integer isOperateAttend;

    /**
     * 操作考勤点到时间
     */
    private String operateAttendTime;

    /**
     * 课次开始日期时间
     */
    private Date sessionStartDate;

    /**
     * 课次结束日期时间
     */
    private Date sessionEndDate;
}
