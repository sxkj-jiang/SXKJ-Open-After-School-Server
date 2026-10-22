package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课时统计-教师课时VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@ApiModel("课时统计-教师课时VO")
public class LessonStatisticsTeacherPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("教师工号")
    private String teacherNo;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("学习周期名称")
    private String termName;

    @ApiModelProperty("总课时数")
    private Integer totalLessonHours;

    @ApiModelProperty("已完成课时数")
    private Integer completedLessonHours;

    @ApiModelProperty("调课课时数")
    private Integer adjustedSessions;

    @ApiModelProperty("代课课时数")
    private Integer substituteSessions;

    public LessonStatisticsTeacherPageVO() {
        this.totalLessonHours = 0;
        this.completedLessonHours = 0;
        this.adjustedSessions = 0;
        this.substituteSessions = 0;
    }
}
