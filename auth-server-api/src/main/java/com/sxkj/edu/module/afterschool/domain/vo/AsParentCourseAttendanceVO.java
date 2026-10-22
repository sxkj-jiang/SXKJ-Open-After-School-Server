package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 家长查询学生课程考勤统计VO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsParentCourseAttendanceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课程总数（按课次维度汇总）")
    private Integer totalCourseCount;

    @ApiModelProperty("出勤率（百分比）")
    private String attendanceRate;

    @ApiModelProperty("出勤率")
    private Double absentRate;

    @ApiModelProperty("课程参与每月打卡趋势")
    private List<AsMonthlyCheckInVO> monthlyCheckInTrend;

    @ApiModelProperty("兴趣分布课后类别占比")
    private List<AsCategoryRatioVO> categoryRatioList;

    @ApiModelProperty("课程平均评分（老师对学生评价的平均分）")
    private String averageScore;

    @ApiModelProperty("教师对学生的评价反馈集合")
    private List<AsTeacherFeedbackVO> teacherFeedbackList;
}
