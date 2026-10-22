package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("查询老师端评价任务详情AO")
public class TeacherEvaluationTaskQueryDetailAO {

    @ApiModelProperty(value = "任务id",required = true)
    @NotNull(message = "任务id不能为空")
    private Long taskId;

    @ApiModelProperty(value = "课程id",required = true)
    @NotNull(message = "课程不能为空")
    private Long courseId;

    @ApiModelProperty("学生id或教师id")
    private Long studentId;

    @ApiModelProperty(value = "评价人账号ID",required = true)
    private Long evaluatorUserId;

}
