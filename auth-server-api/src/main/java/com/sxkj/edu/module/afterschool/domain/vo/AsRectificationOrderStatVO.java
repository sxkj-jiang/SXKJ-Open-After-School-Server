package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 问题整改工单-状态统计VO
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("问题整改工单-状态统计VO")
public class AsRectificationOrderStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long totalCount;

    @ApiModelProperty("待整改数量（状态0）")
    private Long pendingCount;

    @ApiModelProperty("整改中数量（状态1）")
    private Long rectifyingCount;

    @ApiModelProperty("已整改数量（状态2）")
    private Long rectifiedCount;

    @ApiModelProperty("已完成数量（状态3）")
    private Long completedCount;

    @ApiModelProperty("已逾期数量（状态4）")
    private Long overdueCount;
}
