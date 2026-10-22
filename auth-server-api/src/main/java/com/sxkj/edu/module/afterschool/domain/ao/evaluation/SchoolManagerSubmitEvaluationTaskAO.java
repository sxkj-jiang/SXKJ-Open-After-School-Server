package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("提交评价AO")
public class SchoolManagerSubmitEvaluationTaskAO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "任务ID", required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @ApiModelProperty(value = "课程ID", required = true)
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @ApiModelProperty(value = "被评价教师ID", required = true)
    @NotNull(message = "被评价教师ID不能为空")
    private Long teacherId;
    
    @ApiModelProperty("评价总分")
    private BigDecimal totalScore;
    
    @ApiModelProperty("评价文字意见")
    private String commentText;
    
    @ApiModelProperty("答案明细JSON")
    private String answerJson;
}
