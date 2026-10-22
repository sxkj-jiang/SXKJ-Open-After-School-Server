package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 课后服务发布对象。
 * 首次发布 rebuildSessions 默认 true；已发布服务再次发布时必须显式传 true。
 */
@Data
@ApiModel("课后服务发布对象")
public class AsServicePublishAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否发布时重建课次，首次发布默认true；已发布服务再次发布时必须显式传true")
    private Boolean rebuildSessions;

    @ApiModelProperty("发布时临时携带的课程班和排课规则；为空时使用已保存规则")
    @Valid
    private List<AsServiceCourseItemAO> courses;
}
