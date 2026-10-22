package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("学校教务处提交评价AO")
public class SubmitEvaluationAO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "任务ID", required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @ApiModelProperty(value = "课程班ID",required = true)
    @NotNull(message = "课程班ID不能为空")
    private Long courseClassId;

    @ApiModelProperty(value = "被评价教师ID",required = true)
    @NotNull(message = "被评价教师不能为空")
    private Long targetTeacherId;
    
    @ApiModelProperty("评价总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "评价人账号ID")
    private Long evaluatorUserId;
    
    @ApiModelProperty("评价文字意见")
    private String commentText;
    
    @ApiModelProperty(value = "答案明细JSON",required = true)
    @NotBlank(message = "评价结果不能为空")
    private String answerJson;
}
