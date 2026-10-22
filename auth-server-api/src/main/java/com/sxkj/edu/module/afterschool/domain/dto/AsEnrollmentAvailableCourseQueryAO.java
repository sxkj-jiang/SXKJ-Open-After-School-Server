package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 家长端可报名课程查询对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("家长端可报名课程查询对象")
public class AsEnrollmentAvailableCourseQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("课程名称关键词")
    private String keyword;
}
