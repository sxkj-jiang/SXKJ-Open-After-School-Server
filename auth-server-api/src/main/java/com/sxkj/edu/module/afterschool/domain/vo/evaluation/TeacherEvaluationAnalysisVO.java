package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 教师评价分析VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("教师评价分析VO")
public class TeacherEvaluationAnalysisVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;

    @ApiModelProperty("总评价数")
    private Integer totalAnswers;

    @ApiModelProperty("总评价人数")
    private Integer totalEvaluators;

    @ApiModelProperty("总分")
    private Double totalScore;

    @ApiModelProperty("平均分")
    private Double averageScore;

    @ApiModelProperty("最高分")
    private Double maxScore;

    @ApiModelProperty("最低分")
    private Double minScore;

    @ApiModelProperty("分数分布")
    private Map<String, Long> scoreDistribution;

    @ApiModelProperty("评价意见数")
    private Long commentCount;

    @ApiModelProperty("有评价意见率")
    private String hasCommentRate;
}
