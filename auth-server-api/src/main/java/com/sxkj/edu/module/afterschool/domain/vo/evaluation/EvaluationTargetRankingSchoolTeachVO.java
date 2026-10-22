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
@ApiModel("学校评价老师明细VO")
public class EvaluationTargetRankingSchoolTeachVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("排名")
    private Integer rank;

    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;

    @ApiModelProperty("被评价教师名称")
    private String targetTeacherName;

    @ApiModelProperty("课程名称")
    private String courseClassName;

    @ApiModelProperty("评价分")
    private Double totalScore;

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
 