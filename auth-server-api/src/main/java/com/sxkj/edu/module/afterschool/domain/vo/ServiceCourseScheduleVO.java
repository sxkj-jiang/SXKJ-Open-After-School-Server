package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 课后服务课表矩阵视图
 */
@Data
public class ServiceCourseScheduleVO {

    @ApiModelProperty(value = "学期ID")
    private Long termId;

    @ApiModelProperty(value = "学期名称")
    private String termName;

    @ApiModelProperty(value = "表头：周列表（含节次）")
    private List<WeekHeaderVO> weekHeaders;

    @ApiModelProperty(value = "数据行列表")
    private List<ScheduleRowVO> rows;
}
