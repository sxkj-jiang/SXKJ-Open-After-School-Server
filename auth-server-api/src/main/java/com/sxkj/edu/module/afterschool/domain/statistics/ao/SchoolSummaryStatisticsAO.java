package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学校汇总统计查询参数
 *
 * @author system
 * @date 2026-06-16
 */
@Data
@ApiModel("学校汇总统计查询参数")
public class SchoolSummaryStatisticsAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID（为空时取当前账号对应的学校ID）")
    private Long schoolId;
}
