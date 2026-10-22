package com.sxkj.edu.domain.base.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ResearchTemplateUpdateAo {

    @ApiModelProperty("调查模板id 更新时必传")
    private Long id;

    @ApiModelProperty(value = "表头名称", required = true)
    @NotBlank(message = "表头名称不能为空")
    private String headerName;

    @ApiModelProperty(value = "调查活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long researchId;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("计量单位")
    private String measureUnit;

    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "是否必填 1-是 2-否", required = true)
    @NotNull(message = "是否必填不能为空")
    private Integer isNeed;

}
