package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师课时详情-详细课时记录分页VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@ApiModel("教师课时详情-详细课时记录分页VO")
public class TeacherLessonRecordPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("出勤记录ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("上课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("适用年级ID集合JSON")
    private String applicableGradeIds;

    @ApiModelProperty(value = "行政班安排适用班级ID集合，JSON数组")
    private String applicableClassIds;

    @ApiModelProperty("适用年级名称（多个;连接）")
    private String gradeNames;

    @ApiModelProperty("上课时间")
    private String sessionTime;

    @ApiModelProperty("上课类型 1-代课上课 2-正常上课")
    private Integer lessonType;

    @ApiModelProperty("学生出勤率（如：85.5%）")
    private String attendanceRate;
}
