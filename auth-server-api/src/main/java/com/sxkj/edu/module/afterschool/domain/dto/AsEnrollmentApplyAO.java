package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 选课报名AO - 用于家长提交选课报名
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsEnrollmentApplyAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty(value = "服务活动ID", required = true)
    @NotNull(message = "服务活动ID不能为空")
    private Long serviceId;

    @ApiModelProperty(value = "课程班ID")
    private Long courseClassId;

    @ApiModelProperty(value = "报名备注")
    private String remark;

    @ApiModelProperty(value = "家长确认：0-未确认，1-已确认")
    private Integer parentConfirm;
}
