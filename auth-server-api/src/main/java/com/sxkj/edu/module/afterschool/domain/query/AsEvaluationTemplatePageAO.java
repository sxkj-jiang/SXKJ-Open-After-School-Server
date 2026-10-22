package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评价模板分页查询AO
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评价模板分页查询对象")
public class AsEvaluationTemplatePageAO extends PageParamDTO {
    
    @ApiModelProperty("模板编号（精确查询）")
    private String templateCode;

    @ApiModelProperty("模板名称（模糊查询）")
    private String templateName;
    
    @ApiModelProperty("状态：1-启用 0-停用")
    private Integer templateStatus;

    @ApiModelProperty("适用角色（精确匹配，角色ID）")
    private Long applicableRole;

    @ApiModelProperty("创建人姓名（模糊查询）")
    private String createUserName;
}
