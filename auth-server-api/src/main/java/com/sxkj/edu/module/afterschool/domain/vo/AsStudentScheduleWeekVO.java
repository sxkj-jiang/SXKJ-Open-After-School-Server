package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学生课表按周分组VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("学生课表按周分组")
public class AsStudentScheduleWeekVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("第几周（每月第几周）")
    private Integer weekOfMonth;

    @ApiModelProperty("该周的课表项列表（按天分组，每天按上课时间升序）")
    private List<AsStudentScheduleItemVO> scheduleItems;
}
