package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评价模板分页查询DTO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评价模板分页查询对象")
public class AsEvaluationTemplatePageDTO extends PageParamDTO {
    
    @ApiModelProperty("模板名称（模糊查询）")
    private String templateName;
    
    @ApiModelProperty("状态：1启用 0停用")
    private Integer status;
}
