package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师巡课任务分页查询返回VO
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@ApiModel("教师巡课任务分页查询返回VO")
public class AsTeacherPatrolTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("计划ID")
    private Long id;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("巡课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("是否需要登记：0否 1是")
    private Integer isRecord;

    @ApiModelProperty("巡课内容")
    private String patrolContent;
}
