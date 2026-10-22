package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalArticleTypeAO {

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "文章类型名称",required = true)
    @NotBlank(message = "文章类型名称不能为空")
    private String articleTypeName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否启用 0-否 1-是",required = true)
    @NotNull(message = "是否启用不能为空")
    private Integer isEnable;
}
