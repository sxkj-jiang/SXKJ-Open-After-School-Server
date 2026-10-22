package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LessonStatisticsTeacherDetailAO {

    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学期ID",required = true)
    @NotNull(message = "学期ID不能为空")
    private Long termId;

    @ApiModelProperty(value = "教师ID",required = true)
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;
}
