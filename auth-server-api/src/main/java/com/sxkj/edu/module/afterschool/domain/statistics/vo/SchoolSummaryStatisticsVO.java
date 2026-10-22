package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学校汇总统计VO
 *
 * @author system
 * @date 2026-06-16
 */
@Data
@ApiModel("学校汇总统计")
public class SchoolSummaryStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校数（仅超级管理员返回）")
    private Integer schoolCount;

    @ApiModelProperty("在校学生总数")
    private Integer totalStudentCount;

    @ApiModelProperty("已注册学生数")
    private Integer registeredStudentCount;

    @ApiModelProperty("待注册学生数")
    private Integer pendingStudentCount;

    @ApiModelProperty("班级数")
    private Integer classCount;

    @ApiModelProperty("教师数")
    private Integer teacherCount;

    @ApiModelProperty("行政课程总数")
    private Integer adminCourseCount;

    @ApiModelProperty("课后服务课程总数（含公开报名和邀请报名）")
    private Integer afterSchoolCourseCount;

    @ApiModelProperty("学生出勤率（百分比字符串，如 95.5%）")
    private String studentAttendanceRate;

    @ApiModelProperty("通知公告列表（前5条，按日期倒序）")
    private List<NoticeArticleItemVO> noticeList;
}
