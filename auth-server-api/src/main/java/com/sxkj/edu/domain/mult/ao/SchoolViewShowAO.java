package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SchoolViewShowAO {

    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否显示，1-显示，0不显示,默认为1",required = true)
    @NotNull(message = "展示状态不能为空")
    private Integer isShow;
}
