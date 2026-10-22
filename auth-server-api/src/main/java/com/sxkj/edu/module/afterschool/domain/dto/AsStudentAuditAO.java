package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生家长审核请求参数
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("学生家长审核请求参数")
public class AsStudentAuditAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学生ID不能为空")
    @ApiModelProperty(value = "学生ID", required = true)
    private Long studentId;

    @NotBlank(message = "家长联系方式不能为空")
    @ApiModelProperty(value = "家长联系方式（手机号）", required = true)
    private String parentPhone;

    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态：1已通过 2已驳回", required = true)
    private Integer registerStatus;

    @ApiModelProperty("审核意见（驳回时必填）")
    private String auditRemark;
}
