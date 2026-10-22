package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 评价查询分页查询参数
 *
 * @author system
 * @date 2026-05-30
 */
@Data
public class AsSchoolManagerEvaluationQueryDetailAO {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "任务ID",required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @ApiModelProperty("课程班id")
    private Long courseClassId;

    @ApiModelProperty("教师id")
    private Long teacherId;

    @ApiModelProperty("目标类型：1-学校评价老师，2-老师评价学生，3-学生评价老师，4-班主任评价学生")
    private Integer targetType;
}
