package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生课时详情-关联查询汇总VO（学生基本信息+班级+学期+课时统计）
 *
 * @author system
 * @date 2026-06-10
 */
@Data
@ApiModel("学生课时详情汇总VO")
public class StudentDetailSummaryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("性别：0男 1女")
    private Integer gender;

    @ApiModelProperty("头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("年级+行政班级名称")
    private String className;

    @ApiModelProperty("入学年份")
    private Integer enrollmentYear;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学期开始日期")
    private Date termStartDate;

    @ApiModelProperty("学期结束日期")
    private Date termEndDate;

    @ApiModelProperty("总课时")
    private Integer totalLessonHours;

    @ApiModelProperty("出勤课时")
    private Integer attendanceHours;

    @ApiModelProperty("缺勤请假课时")
    private Integer absenceHours;
}
