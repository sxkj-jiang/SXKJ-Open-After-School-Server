package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师审核请求参数
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("教师审核请求参数")
public class AsTeacherAuditAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "教师ID不能为空")
    @ApiModelProperty(value = "教师ID", required = true)
    private Long id;

    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态：1已通过 2已驳回 3-待审核", required = true)
    private Integer registerStatus;

    @ApiModelProperty("审核意见（驳回时必填）")
    private String auditRemark;
}
