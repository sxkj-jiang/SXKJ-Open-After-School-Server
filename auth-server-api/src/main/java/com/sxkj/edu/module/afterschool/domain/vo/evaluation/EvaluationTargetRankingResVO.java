package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理端-评价任务-详情V
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("管理端-评价任务-详情VO")
public class EvaluationTargetRankingResVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("排名")
    private Integer rank;

    @ApiModelProperty("评价任务id")
    private Long taskId;

    @ApiModelProperty("评价任务名称")
    private String taskName;

    @ApiModelProperty("授课教师id")
    private Long teacherId;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseClassName;

    @ApiModelProperty("应评总人数")
    private Long evaluatorSumCount;

    @ApiModelProperty("已评价人数")
    private Long evaluatorCount;

    @ApiModelProperty("平均分")
    private Double averageScore;

    @ApiModelProperty("完成率")
    private String completionRate;

    @ApiModelProperty("状态")
    private String status;


}
 