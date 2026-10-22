package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师课程汇总统计VO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsTeacherCourseSummaryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("总课时（课次总数）")
    private Integer totalSessions;

    @ApiModelProperty("已完成课时（教师已出勤）")
    private Integer completedSessions;

    @ApiModelProperty("出勤率保留两位小数")
    private Double attendanceRate;

    @ApiModelProperty("出勤率（百分比）")
    private String attendanceRateRes;

    @ApiModelProperty("学生数")
    private Integer studentCount;
}
