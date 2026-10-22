 package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

 /**
 * 评价答案管理-查询评价老师的答案
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("查询评价学生结果")
public class AsEvaluationAnswerSchoolForStuQueryAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;

     @NotNull(message = "评价人用户id不能为空")
     @ApiModelProperty(value = "评价人用户id", required = true)
     private Long userId;

     @NotNull(message = "课程班ID不能为空")
     @ApiModelProperty(value = "课程班ID", required = true)
     private Long courseClassId;

     @NotNull(message = "被评价学生ID不能为空")
     @ApiModelProperty(value = "被评价学生ID", required = true)
     private Long targetStudentId;

}
