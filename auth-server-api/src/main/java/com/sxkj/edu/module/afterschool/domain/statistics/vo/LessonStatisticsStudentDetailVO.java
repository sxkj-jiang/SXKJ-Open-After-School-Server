package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclTermVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课时统计-学生课时详情VO
 *
 * @author system
 * @date 2026-06-10
 */
@Data
@ApiModel("课时统计-学生课时详情VO")
public class LessonStatisticsStudentDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ============ 学生基本信息 ============

    @ApiModelProperty("学生头像")
    private FileVO avatar;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("性别（男、女）")
    private String genderName;

    @ApiModelProperty("入学年份")
    private Integer enrollmentYear;

    @ApiModelProperty("年级+行政班级名称")
    private String className;

    // ============ 学习周期信息 ============

    @ApiModelProperty("学习周期ID")
    private Long termId;

    @ApiModelProperty("学习周期名称")
    private String termName;

    // ============ 课时统计 ============

    @ApiModelProperty("总课时")
    private Integer totalLessonHours;

    @ApiModelProperty("出勤课时")
    private Integer attendanceHours;

    @ApiModelProperty("缺勤请假课时")
    private Integer absenceHours;

    @ApiModelProperty("出勤率（如：85.5%）")
    private String attendanceRate;

    // ============ 环比上月 ============

    @ApiModelProperty("较上月趋势：up-上升 down-降低 flat-持平")
    private String trend;

    @ApiModelProperty("较上月变化比率（如：5.2%）")
    private String trendRate;

    // ============ 出勤趋势 ============

    @ApiModelProperty("出勤趋势列表")
    private List<StudentAttendanceTrendItemVO> attendanceTrend;

    // ============ 课时分布 ============

    @ApiModelProperty("课时分布列表")
    private List<StudentLessonDistributionItemVO> lessonDistribution;

    @ApiModelProperty("学期筛选列表")
    private List<EduclTermVO> termList;
}
