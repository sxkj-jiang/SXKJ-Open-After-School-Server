package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 被评价对象进度VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("被评价对象进度VO")
public class TargetProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;

    @ApiModelProperty("答案数")
    private Long answerCount;

    @ApiModelProperty("评价人数")
    private Long evaluatorCount;
}
