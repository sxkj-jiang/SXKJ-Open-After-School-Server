package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价查询详情VO
 *
 * @author system
 * @date 2026-05-30
 */
@Data
public class AsEvaluationQueryDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("人数")
    private Integer enrolledCount;

    @ApiModelProperty("上课时间")
    private String courseTime;

    @ApiModelProperty("课程简介")
    private String courseIntro;

    @ApiModelProperty("学生评价列表（教师评价学生）")
    private List<EvaluationTargetRankingVO> studentEvaluationList;

    @ApiModelProperty("教师评价列表（学生评价老师）")
    private List<EvaluationTargetRankingVO> teacherEvaluationList;

    @ApiModelProperty("管理员评价列表")
    private List<EvaluationTargetRankingVO> adminEvaluationList;
}
