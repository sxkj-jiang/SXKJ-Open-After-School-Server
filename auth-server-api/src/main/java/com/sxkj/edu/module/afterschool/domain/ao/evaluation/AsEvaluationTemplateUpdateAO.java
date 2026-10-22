package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 评价模板更新AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("评价模板更新对象")
public class AsEvaluationTemplateUpdateAO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "模板ID不能为空")
    @ApiModelProperty(value = "模板ID", required = true)
    private Long id;
    
    @ApiModelProperty(value = "模板名称",required = true)
    @ChineseTitleAnno
    private String templateName;

    @ApiModelProperty(value = "模板描述")
    private String templateDesc;
    
    @ApiModelProperty(value = "指标体系ID")
    private Long indicatorSystemId;
    
    @ApiModelProperty(value = "试题")
    private String templateConfig;
    
    @ApiModelProperty(value = "适用角色JSON数组（角色ID，如[1001,1002]）",required = true)
    @NotBlank(message = "适用角色数组不能为空")
    private String applicableRoles;
    
    @ApiModelProperty(value = "状态：0-停用 1-启用")
    private Integer templateStatus;

    @NotNull(message = "计分类型不能为空")
    @ApiModelProperty(value = "计分类型：1-计分 0-不计分", required = true)
    private Integer scoreStatus;
}
