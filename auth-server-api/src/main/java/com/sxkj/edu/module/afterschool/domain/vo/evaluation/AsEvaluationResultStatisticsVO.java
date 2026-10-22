package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评价结果统计VO
 */
@Data
@ApiModel("评价结果统计视图对象")
public class AsEvaluationResultStatisticsVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("任务ID")
    private Long taskId;
    
    @ApiModelProperty("任务名称")
    private String taskName;
    
    @ApiModelProperty("总参与人数")
    private Integer totalParticipants;
    
    @ApiModelProperty("已完成评价人数")
    private Integer completedCount;
    
    @ApiModelProperty("完成率")
    private String completionRate;
    
    @ApiModelProperty("题目统计列表")
    private List<QuestionStatisticsVO> questionStatistics;
    
    /**
     * 题目统计内部类
     */
    @Data
    public static class QuestionStatisticsVO implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @ApiModelProperty("题目ID")
        private String questionId;

        @ApiModelProperty("试题编号")
        private String questionCode;
        
        @ApiModelProperty("题目内容")
        private String questionContent;
        
        @ApiModelProperty("题目类型")
        private Integer questionType;
        
        @ApiModelProperty("平均得分")
        private Double averageScore;
        
        @ApiModelProperty("最高分")
        private Integer maxScore;
        
        @ApiModelProperty("最低分")
        private Integer minScore;
        
        @ApiModelProperty("选项分布（JSON）")
        private String optionDistribution;
    }
}
