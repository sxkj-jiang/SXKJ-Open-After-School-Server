package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 作息管理方案分页查询参数
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("作息管理方案分页查询参数")
public class AsScheduleTemplateQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty(value = "方案类型：1作息管理 2课后时间管理",required = true)
    private Integer templateType;

    @ApiModelProperty("方案名称(模糊)")
    private String templateName;
}
