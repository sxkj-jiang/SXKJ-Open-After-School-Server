package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后服务发布结果。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务发布结果")
public class AsServicePublishResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("发布后服务状态")
    private Integer serviceStatus;

    @ApiModelProperty("发布后服务状态文案")
    private String serviceStatusName;

    @ApiModelProperty("生成课次数")
    private Integer generatedSessionCount = 0;

    @ApiModelProperty("初始化未选课记录数")
    private Integer initializedCount = 0;

    @ApiModelProperty("已存在选课记录数")
    private Integer existedEnrollmentCount = 0;

    @ApiModelProperty("行政班安排生成数")
    private Integer classAssignedCount = 0;
}
