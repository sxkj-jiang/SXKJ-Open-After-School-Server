package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师审核状态汇总VO
 *
 * @author system
 * @date 2026-06-19
 */
@Data
@ApiModel("教师审核状态汇总")
public class AsTeacherAuditSummaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long allCount = 0L;

    @ApiModelProperty("待注册数量（register_status=0）")
    private Long pendingRegisterCount = 0L;

    @ApiModelProperty("已通过数量（register_status=1）")
    private Long approvedCount = 0L;

    @ApiModelProperty("已驳回数量（register_status=2）")
    private Long rejectedCount = 0L;

    @ApiModelProperty("待审核数量（register_status=3）")
    private Long pendingAuditCount = 0L;
}
