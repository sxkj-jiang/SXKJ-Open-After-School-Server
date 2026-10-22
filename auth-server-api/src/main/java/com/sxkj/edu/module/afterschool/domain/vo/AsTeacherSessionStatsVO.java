package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师课时统计VO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsTeacherSessionStatsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("总课时数")
    private Integer totalSessions;

    @ApiModelProperty("已完成课时数")
    private Integer completedSessions;

    @ApiModelProperty("调课课时数")
    private Integer adjustedSessions;

    @ApiModelProperty("代课课时数")
    private Integer substituteSessions;

    @ApiModelProperty("优秀占比（评分>80，百分比保留两位小数）")
    private Double excellentRatio;

    @ApiModelProperty("良好占比（60<=评分<=80，百分比保留两位小数）")
    private Double goodRatio;

    @ApiModelProperty("不及格占比（评分<60，百分比保留两位小数）")
    private Double failRatio;

    @ApiModelProperty("优秀占比（评分>80，百分比保留两位小数）")
    private String excellentRatioRes;

    @ApiModelProperty("良好占比（60<=评分<=80，百分比保留两位小数）")
    private String goodRatioRes;

    @ApiModelProperty("不及格占比（评分<60，百分比保留两位小数）")
    private String failRatioRes;
}
