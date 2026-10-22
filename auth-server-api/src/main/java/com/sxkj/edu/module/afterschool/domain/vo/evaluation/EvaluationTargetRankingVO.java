package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 被评价对象排名VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("被评价对象排名VO")
public class EvaluationTargetRankingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("排名")
    private Integer rank;

    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;

    @ApiModelProperty("被评价教师名称")
    private String targetTeacherName;

    @ApiModelProperty("被评价教师职务")
    private String positionName;
    
    @ApiModelProperty("被评价学生ID")
    private Long targetStudentId;

    @ApiModelProperty("被评价学生名称")
    private String targetStudentName;

    @ApiModelProperty("课程名称")
    private String courseClassName;

    @ApiModelProperty("答案数")
    private Integer answerCount;

    @ApiModelProperty("评价人数")
    private Long evaluatorCount;

    @ApiModelProperty("评价得分")
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

    @ApiModelProperty("提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    @ApiModelProperty("评价状态：0-未评价 1-已评价")
    private Integer evaluationStatus;

    @ApiModelProperty("评价状态描述")
    private String evaluationStatusDesc;
}
 