package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("任课老师端评价学生任务列表AO")
public class TeacherEvaluationTaskListAO extends PageParamDTO {

    @ApiModelProperty("Tab类型：0-全部 1-待评价 2-已评价")
    private Integer tabType;

    @ApiModelProperty("评价教师id")
    private Long evaluateTeacherId;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;
}
