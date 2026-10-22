package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 评价模板新增AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价模板新增对象")
public class AsEvaluationTemplateAddAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty(value = "模板名称", required = true)
    @ChineseTitleAnno
    private String templateName;

    @ApiModelProperty(value = "模板描述")
    private String templateDesc;

    @NotNull(message = "指标体系ID不能为空")
    @ApiModelProperty(value = "指标体系ID", required = true)
    private Long indicatorSystemId;
    
    @ApiModelProperty(value = "适用角色JSON数组（角色ID，如[1001,1002]）")
    private String applicableRoles;
    
    @ApiModelProperty(value = "题目/选项/分值配置，JSON结构")
    private String templateConfig;
    
    @ApiModelProperty(value = "状态：1-启用 0-停用，默认1")
    private Integer templateStatus;

    @NotNull(message = "计分类型不能为空")
    @ApiModelProperty(value = "计分类型：1-计分 0-不计分", required = true)
    private Integer scoreStatus;
}
