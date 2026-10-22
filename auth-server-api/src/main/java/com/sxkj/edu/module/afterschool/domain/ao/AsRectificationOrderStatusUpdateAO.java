package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AsRectificationOrderStatusUpdateAO {

    @NotNull(message = "整改单ID不能为空")
    @ApiModelProperty(value = "整改单ID",required = true)
    private Long id;

    @NotNull(message = "整改单状态不能为空")
    @ApiModelProperty(value = "整改单状态 0待整改 1整改中 2已整改 3已完成 4已逾期",required = true)
    private Integer status;
}
