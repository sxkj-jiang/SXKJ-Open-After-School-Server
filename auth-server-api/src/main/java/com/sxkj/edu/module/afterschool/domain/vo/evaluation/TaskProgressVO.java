package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 任务整体进度VO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("任务整体进度VO")
public class TaskProgressVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("总参与人数")
    private Long totalParticipants;

    @ApiModelProperty("应参与人数")
    private Long shouldParticipants;

    @ApiModelProperty("参与率")
    private String participationRate;

    @ApiModelProperty("总答案数")
    private Long totalAnswers;

    @ApiModelProperty("总教师数")
    private Long totalTeachers;
}
