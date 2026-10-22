package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseScheduleDetailListVO {

    @ApiModelProperty(value = "年级名称")
    private String gradeName;

    @ApiModelProperty(value = "年级id")
    private Long gradeId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "班级id")
    private Long classId;

    @ApiModelProperty(value = "周次列表")
    private List<CourseScheduleWeekVO> teachVOList;

}
