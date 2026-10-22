package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评价查询-学校评价老师详情
 *
 * @author system
 * @date 2026-05-30
 */
@Data
public class AsEvaluationQueryDetailForSchoolTeacherVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("评价任务名称")
    private String taskName;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程类别")
    private String categoryName;

    @ApiModelProperty("选课类型")
    private String enrollTypeDesc;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("课程班人数")
    private Integer enrolledCount;

    @ApiModelProperty("开课开始时间，格式为：2026-05-30 14:00:00")
    private String courseStartDate;

    @ApiModelProperty("开课结束时间，格式为：2026-05-30 14:00:00")
    private String courseEndTime;

    @ApiModelProperty("课程简介")
    private String courseIntro;

    @ApiModelProperty("学校评价老师列表")
    private List<EvaluationTargetRankingSchoolTeachVO> adminTeachEvaluationList;

    @ApiModelProperty("教师评价学生任务列表")
    private List<EvaluationTargetRankingTeachStuVO> studentEvaluationList;

    @ApiModelProperty("班主任评价学生任务列表")
    private List<EvaluationTargetRankingTeachStuVO> classTeacherEvaluationList;

    @ApiModelProperty("学生评价老师列表")
    private List<EvaluationTargetRankingSchoolTeachVO> teacherEvaluationList;
}
