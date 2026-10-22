package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分配课程候选查询对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("分配课程候选查询对象")
public class AsEnrollmentAssignOptionsQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;
}
