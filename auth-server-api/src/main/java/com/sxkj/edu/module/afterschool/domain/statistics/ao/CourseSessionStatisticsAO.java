package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 课程课时统计查询AO
 *
 * @author system
 * @date 2026-06-25
 */
@Data
public class CourseSessionStatisticsAO {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;
}
