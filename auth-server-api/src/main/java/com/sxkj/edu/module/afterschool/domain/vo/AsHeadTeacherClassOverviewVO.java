package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 班主任统计概览VO - 自然班级维度
 *
 * @author system
 * @date 2026-06-13
 */
@Data
@ApiModel(value = "班主任统计概览VO", description = "班主任统计概览-自然班级维度")
public class AsHeadTeacherClassOverviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("自然班级ID")
    private Long classId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("自然班级名称")
    private String className;

    @ApiModelProperty("自然班级人数")
    private Integer classStudentCount;

    @ApiModelProperty("课后在校人数")
    private Integer afterSchoolStudentCount;

    @ApiModelProperty("出勤率(百分比字符串，如'95.50%')")
    private String attendanceRate;
}
