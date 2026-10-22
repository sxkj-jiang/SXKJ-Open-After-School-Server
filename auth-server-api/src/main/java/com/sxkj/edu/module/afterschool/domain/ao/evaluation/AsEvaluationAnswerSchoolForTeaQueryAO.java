 package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

 /**
 * 评价答案管理-查询评价老师的答案
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("查询评价老师的答案")
public class AsEvaluationAnswerSchoolForTeaQueryAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;

     @NotNull(message = "评价人用户ID不能为空")
     @ApiModelProperty(value = "评价人用户id", required = true)
     private Long userId;

     @NotNull(message = "课程班ID不能为空")
     @ApiModelProperty(value = "课程班ID", required = true)
     private Long courseClassId;

     @NotNull(message = "被评价教师ID不能为空")
     @ApiModelProperty(value = "被评价教师ID", required = true)
     private Long targetTeacherId;

}
