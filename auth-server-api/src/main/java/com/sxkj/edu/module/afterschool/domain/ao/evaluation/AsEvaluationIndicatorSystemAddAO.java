package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 评价指标体系新增AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价指标体系新增对象")
public class AsEvaluationIndicatorSystemAddAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;
    
    @NotBlank(message = "指标体系名称不能为空")
    @ApiModelProperty(value = "指标体系名称", required = true)
    private String systemName;
    
    @ApiModelProperty("指标体系描述")
    private String systemDesc;
    
    @ApiModelProperty("适用角色JSON")
    private String applicableRoles;
    
    @ApiModelProperty("指标配置JSON")
    private String indicatorConfig;
    
    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态：1-启用 0-停用", required = true)
    private Integer status;
}
