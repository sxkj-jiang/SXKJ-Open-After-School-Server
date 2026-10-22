package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 方案下拉列表视图对象
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("方案下拉列表视图对象")
public class AsScheduleTemplateListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方案ID")
    private Long id;

    @ApiModelProperty("方案名称")
    private String templateName;

    @ApiModelProperty("方案类型：1作息管理 2课后时间管理")
    private Integer templateType;

    @ApiModelProperty("启用状态：1启用 0停用")
    private Integer enabledStatus;
}
