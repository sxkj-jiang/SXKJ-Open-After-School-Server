package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 方案下拉列表查询参数
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("方案下拉列表查询参数")
public class AsScheduleTemplateListQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("方案名称(模糊)")
    private String templateName;

    @ApiModelProperty("方案类型：1作息管理 2课后时间管理")
    private Integer templateType;
}
