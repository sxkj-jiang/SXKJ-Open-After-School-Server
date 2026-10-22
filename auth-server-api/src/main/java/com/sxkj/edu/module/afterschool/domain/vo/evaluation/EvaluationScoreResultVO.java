package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 评价得分结果VO（含每题得分明细）
 */
@Data
@ApiModel("评价得分结果VO")
public class EvaluationScoreResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总分")
    private BigDecimal totalScore;

    @ApiModelProperty("更新后的答案JSON（含每题得分）")
    private String updatedAnswerJson;

    @ApiModelProperty("每题得分明细")
    private List<QuestionScoreItemVO> questionScores;

    @Data
    @ApiModel("单题得分明细")
    public static class QuestionScoreItemVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("题目ID")
        private Long questionId;

        @ApiModelProperty("题目标题")
        private String title;

        @ApiModelProperty("百分比得分（0-100）")
        private BigDecimal score;
    }
}
