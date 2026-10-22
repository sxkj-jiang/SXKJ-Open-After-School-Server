package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生学习成果分页查询 AO
 *
 * @author system
 * @date 2026-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生学习成果分页查询参数")
public class AsStudentLearningAchievementQueryAO extends PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("自然班ID")
    private Long classId;

    @ApiModelProperty("学生姓名或学号（模糊）")
    private String studentKeyword;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty(value = "课次开始日期，格式：yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionStartDate;

    @ApiModelProperty(value = "课次结束日期，格式：yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionEndDate;

    @ApiModelProperty("学期ID")
    private Long termId;


}
