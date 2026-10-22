package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师调课状态数量。
 */
@Data
@ApiModel("教师调课状态数量")
public class AsTeacherAdjustStatusCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long allCount = 0L;

    @ApiModelProperty("待处理数量")
    private Long pendingCount = 0L;

    @ApiModelProperty("已同意数量")
    private Long approvedCount = 0L;

    @ApiModelProperty("已拒绝数量")
    private Long rejectedCount = 0L;

    @ApiModelProperty("已撤回数量")
    private Long withdrawnCount = 0L;
}
