package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务选课统计。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务选课统计")
public class AsServiceEnrollmentStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("已选课人数")
    private Integer selectedCount = 0;

    @ApiModelProperty("未选课人数")
    private Integer unselectedCount = 0;

    @ApiModelProperty("待确认人数")
    private Integer pendingCount = 0;

    @ApiModelProperty("已取消人数")
    private Integer cancelledCount = 0;
}
