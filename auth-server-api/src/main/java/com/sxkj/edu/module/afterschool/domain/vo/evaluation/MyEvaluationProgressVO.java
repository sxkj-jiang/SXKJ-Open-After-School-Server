package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 我的评价进度VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("我的评价进度VO")
public class MyEvaluationProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("评价人ID")
    private Long evaluatorId;

    @ApiModelProperty("已回答数")
    private Long answeredCount;

    @ApiModelProperty("总题数")
    private Long totalCount;

    @ApiModelProperty("进度百分比")
    private String progress;

    @ApiModelProperty("是否已完成")
    private Boolean completed;
}
