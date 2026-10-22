package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程课时统计数据总览VO
 *
 * @author system
 * @date 2026-06-25
 */
@Data
public class CourseSessionStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总课时数")
    private Integer totalSessions;

    @ApiModelProperty("已完成课时数")
    private Integer completedSessions;

    @ApiModelProperty("剩余课时数")
    private Integer remainingSessions;

    @ApiModelProperty("完成进度（百分比字符串，如：75.5%）")
    private String completionRateRes;
}
