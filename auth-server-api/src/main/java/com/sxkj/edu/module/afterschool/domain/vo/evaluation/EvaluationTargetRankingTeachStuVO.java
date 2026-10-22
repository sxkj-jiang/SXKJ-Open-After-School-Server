package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 被评价对象VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("老师评价学生明细VO")
public class EvaluationTargetRankingTeachStuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("排名")
    private Integer rank;

    @ApiModelProperty("学生ID")
    private Long targetStudentId;

    @ApiModelProperty("学生姓名")
    private String targetStudentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("学生年级")
    private String studentGrade;

    @ApiModelProperty("学生班级")
    private String studentClass;

    @ApiModelProperty("课程名称多个用逗号分隔")
    private String courseClassName;

    @ApiModelProperty("评分")
    private Double totalScore;

    @ApiModelProperty("平均分")
    private Double averageScore;

    @ApiModelProperty("完成率")
    private String completionRate;

    @ApiModelProperty("状态")
    private String status;

    // 以下字段用于评价明细列表场景（无targetTeacherId时）
    @ApiModelProperty("答案ID")
    private Long answerId;

    @ApiModelProperty("评价人用户ID")
    private Long evaluatorUserId;

    @ApiModelProperty("评价人名称")
    private String evaluatorName;

    @ApiModelProperty("评价意见")
    private String commentText;

    @ApiModelProperty("评价提交时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    @ApiModelProperty("评价状态：0-未评价 1-已评价")
    private Integer evaluationStatus;

    @ApiModelProperty("评价状态描述")
    private String evaluationStatusDesc;

    @ApiModelProperty(value = "学生年级ID（内部字段，不返回前端）", hidden = true)
    @JsonIgnore
    private Long studentGradeId;

    @ApiModelProperty(value = "学生班级ID（内部字段，不返回前端）", hidden = true)
    @JsonIgnore
    private Long studentClassId;
}
 