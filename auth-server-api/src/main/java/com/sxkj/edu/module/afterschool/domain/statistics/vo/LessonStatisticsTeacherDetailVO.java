package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.sxkj.edu.domain.vo.FileVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课时统计-教师课时详情VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@ApiModel("课时统计-教师课时详情VO")
public class LessonStatisticsTeacherDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师头像")
    private FileVO avatar;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("工号")
    private String teacherNo;

    @ApiModelProperty("性别（男、女）")
    private String genderName;

    @ApiModelProperty("入职年份")
    private Long registYear;

    @ApiModelProperty("在职状态（1在职 2离职）")
    private Integer employeeStatus;

    @ApiModelProperty("周期名称")
    private String termName;

    @ApiModelProperty("总课时")
    private Integer totalLessonHours;

    @ApiModelProperty("已完成课时")
    private Integer completedLessonHours;

    @ApiModelProperty("调课课时数")
    private Integer adjustedSessions;

    @ApiModelProperty("代课课时数")
    private Integer substituteSessions;

    @ApiModelProperty("课时趋势")
    private List<TeacherLessonTrendItemVO> lessonTrend;

    @ApiModelProperty("课时分布")
    private List<StatisticsCommonCountVO> lessonDistribution;

    public LessonStatisticsTeacherDetailVO() {
        this.totalLessonHours = 0;
        this.completedLessonHours = 0;
        this.adjustedSessions = 0;
        this.substituteSessions = 0;
    }
}
