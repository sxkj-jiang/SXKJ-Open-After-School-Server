package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课时统计-学生课时分页VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@ApiModel("课时统计-学生课时分页VO")
public class LessonStatisticsStudentPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生稳定身份ID(唯一)")
    private Long studentIdentityId;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学习周期名称")
    private String termName;

    @ApiModelProperty("课程班名称（多个用;隔开）")
    private String courseClassNames;

    @ApiModelProperty("课程类别名称（多个用;隔开）")
    private String courseCategoryNames;

    @ApiModelProperty("总课时数")
    private Integer totalLessonHours;

    @ApiModelProperty("出勤课时数")
    private Integer attendanceHours;

    @ApiModelProperty("缺勤/请假课时数")
    private Integer absenceHours;

    @ApiModelProperty("出勤率（如：85.5%）")
    private String attendanceRate;
}
