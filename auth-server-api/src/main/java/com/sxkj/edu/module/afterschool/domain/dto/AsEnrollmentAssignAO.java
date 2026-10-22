package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生选课分配对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课分配对象")
public class AsEnrollmentAssignAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("选课记录ID")
    private Long enrollmentId;

    @ApiModelProperty("服务ID；destinationType=1时必填，非课程去向可从enrollmentId推导")
    private Long serviceId;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty("课程班ID；destinationType=1课程班时必填")
    private Long courseClassId;

    @ApiModelProperty("去向类型：1课程班 2回家 3培训机构 4留班 5行政课 6其他")
    private Integer destinationType;

    @ApiModelProperty("备注")
    private String remark;
}
