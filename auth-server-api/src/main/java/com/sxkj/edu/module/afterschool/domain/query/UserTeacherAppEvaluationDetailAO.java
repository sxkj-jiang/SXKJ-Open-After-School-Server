package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("任课老师端查询我收到的评价-学生和学校")
public class UserTeacherAppEvaluationDetailAO {

    @ApiModelProperty(value = "评价类型：1-学生评价 2-管理员评价",required = true)
    @NotNull(message = "评价类型不能为空")
    private Integer tabType;

    @ApiModelProperty(value = "任务ID",required = true)
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @ApiModelProperty(value = "课程班级ID",required = true)
    @NotNull(message = "课程班不能为空")
    private Long courseClassId;
}
