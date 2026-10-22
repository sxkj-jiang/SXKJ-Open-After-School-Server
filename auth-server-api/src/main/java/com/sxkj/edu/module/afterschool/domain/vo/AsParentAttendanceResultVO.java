package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 家长查询考勤结果VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
public class AsParentAttendanceResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("汇总统计")
    private AsParentAttendanceSummaryVO summary;

    @ApiModelProperty("详情记录列表")
    private List<AsParentAttendanceDetailVO> details;
}
