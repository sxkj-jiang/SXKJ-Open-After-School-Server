package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseScheduleDetailListQueryAO {

    @ApiModelProperty(value = "课后服务id")
    private Long id;

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "班级id")
    private Long classId;

    @ApiModelProperty(value = "年级id")
    private Long gradeId;
}
