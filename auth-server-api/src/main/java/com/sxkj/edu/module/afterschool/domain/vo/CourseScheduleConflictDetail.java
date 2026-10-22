package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.enums.CourseScheduleConflictTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程排课冲突明细。
 */
@Data
@ApiModel("课程排课冲突明细")
public class CourseScheduleConflictDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("冲突类型")
    private CourseScheduleConflictTypeEnum conflictType;

    @ApiModelProperty("冲突提示文案")
    private String message;

    @ApiModelProperty("当前课程班ID")
    private Long courseClassId;

    @ApiModelProperty("当前课程班名称")
    private String courseClassName;

    @ApiModelProperty("冲突课程班ID")
    private Long conflictCourseClassId;

    @ApiModelProperty("冲突课程班名称")
    private String conflictCourseClassName;
}
