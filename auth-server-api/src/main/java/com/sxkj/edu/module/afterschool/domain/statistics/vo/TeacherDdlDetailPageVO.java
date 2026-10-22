package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师统计-点到率详情分页VO
 *
 * @author system
 * @date 2026-06-17
 */
@Data
@ApiModel("教师统计-点到率详情分页VO")
public class TeacherDdlDetailPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("适用年级ID集合JSON")
    private String applicableGradeIds;

    @ApiModelProperty("年级名称（多个;连接）")
    private String gradeNames;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("操作考勤点到时间")
    private String operateAttendTime;

    @ApiModelProperty("是否操作考勤点到 0-否 1-是")
    private Integer isOperateAttend;
}
