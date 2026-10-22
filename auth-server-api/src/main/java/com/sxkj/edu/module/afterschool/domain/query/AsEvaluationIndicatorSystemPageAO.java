package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评价指标体系分页查询AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评价指标体系分页查询对象")
public class AsEvaluationIndicatorSystemPageAO extends PageParamDTO {
    
    @ApiModelProperty("指标体系名称（模糊查询）")
    private String systemName;
    
    @ApiModelProperty("适用角色编码")
    private Integer roleCode;
    
    @ApiModelProperty("状态：1-启用 0-停用")
    private Integer status;
}
