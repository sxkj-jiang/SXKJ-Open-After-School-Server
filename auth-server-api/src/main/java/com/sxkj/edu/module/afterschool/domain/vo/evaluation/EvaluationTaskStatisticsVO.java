package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 评价任务统计VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价任务统计VO")
public class EvaluationTaskStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("总答案数")
    private Integer totalAnswers;

    @ApiModelProperty("教师答案数统计(key: 教师ID, value: 答案数)")
    private Map<Long, Long> teacherAnswerCount;

    @ApiModelProperty("评价人答案数统计(key: 评价人ID, value: 答案数)")
    private Map<Long, Long> evaluatorAnswerCount;
}
