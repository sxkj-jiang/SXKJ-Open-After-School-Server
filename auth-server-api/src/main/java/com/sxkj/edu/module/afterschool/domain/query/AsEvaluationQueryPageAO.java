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
public class AsEvaluationQueryPageAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程类别")
    private Long categoryId;

    @ApiModelProperty("年级")
    private Long gradeId;

    @ApiModelProperty("教师")
    private Long teacherId;

    @ApiModelProperty(value = "评价类型，2-学生家长，3-教师，4-学校评价",required = true)
    @NotNull(message = "评价类型不能为空")
    private Long evaluationType;
}
