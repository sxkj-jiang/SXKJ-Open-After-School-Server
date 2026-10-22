package com.sxkj.edu.module.afterschool.domain.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 管理端-评价查询-班主任对学生的评价参数
 *
 * @author system
 * @date 2026-05-30
 */
@Data
public class AsSchoolManagerEvaluationTeacherLeadQdAO {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "任务ID",required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @ApiModelProperty(value = "教师id",required = true)
    @NotNull(message = "教师id不能为空")
    private Long teacherId;

    @ApiModelProperty("目标类型：1-学校评价老师，2-老师评价学生，3-学生评价老师，4-班主任评价学生")
    private Integer targetType;
}
