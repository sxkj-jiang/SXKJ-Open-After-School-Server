package com.sxkj.edu.module.afterschool.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 班主任端-评价任务学生列表查询入参
 *
 * @author system
 * @date 2026-07-08
 */
@Data
@ApiModel("班主任端评价任务学生列表查询AO")
public class HeadteacherEvaluationStudentQueryAO {

    @NotNull(message = "评价任务ID不能为空")
    @ApiModelProperty(value = "评价任务ID", required = true)
    private Long taskId;

    @ApiModelProperty(value = "班主任用户ID")
    private Long userId;
}
