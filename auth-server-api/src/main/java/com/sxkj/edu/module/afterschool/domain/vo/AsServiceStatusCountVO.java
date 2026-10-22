package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 排课管理各 Tab 状态数量汇总。
 */
@Data
@ApiModel("课后服务状态数量汇总")
public class AsServiceStatusCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long allCount = 0L;

    @ApiModelProperty("未发布数量")
    private Long unpublishedCount = 0L;

    @ApiModelProperty("待报名数量")
    private Long pendingEnrollCount = 0L;

    @ApiModelProperty("报名中数量")
    private Long enrollingCount = 0L;

    @ApiModelProperty("待开课数量")
    private Long pendingCourseCount = 0L;

    @ApiModelProperty("进行中数量")
    private Long runningCount = 0L;

    @ApiModelProperty("已结束数量")
    private Long finishedCount = 0L;
}
