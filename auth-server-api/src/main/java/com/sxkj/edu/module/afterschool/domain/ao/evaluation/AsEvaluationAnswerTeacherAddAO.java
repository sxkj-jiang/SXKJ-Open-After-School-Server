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
@ApiModel("老师评价学生对象")
public class AsEvaluationAnswerTeacherAddAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;
    
    @ApiModelProperty(value = "课程班ID",required = true)
    @NotNull(message = "课程班ID不能为空")
    private Long courseClassId;
    
    @ApiModelProperty(value = "评价人账号ID")
    private Long evaluatorUserId;
    
    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;

    @ApiModelProperty(value = "被评价学生ID",required = true)
    @NotNull(message = "被评价学生不能为空")
    private Long targetStudentId;
    
    @ApiModelProperty("评价总分")
    private BigDecimal totalScore;
    
    @ApiModelProperty(value = "评价结果明细JSON",required = true)
    @NotBlank(message = "评价结果不能为空")
    private String answerJson;
    
    @ApiModelProperty("评价文字意见")
    private String commentText;
}
