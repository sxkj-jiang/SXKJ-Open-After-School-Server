 package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

 /**
 * 评价答案新增AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价答案新增对象")
public class AsEvaluationAnswerAddAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;
    
    @ApiModelProperty("课程班ID")
    private Long courseClassId;
    
    @NotNull(message = "评价人账号ID不能为空")
    @ApiModelProperty(value = "评价人账号ID", required = true)
    private Long evaluatorUserId;
    
    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;

    @ApiModelProperty("被评价学生ID")
    private Long targetStudentId;
    
    @ApiModelProperty("评价总分")
    private BigDecimal totalScore;
    
    @ApiModelProperty("评价作答明细JSON")
    private String answerJson;
    
    @ApiModelProperty("评价文字意见")
    private String commentText;
}
