package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 待办任务-完成待办参数
 */
@Data
@ApiModel("待办任务-完成待办参数")
public class AsTodoTaskCompleteAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID", required = true)
    private Long id;

    @NotNull(message = "任务状态不能为空")
    @ApiModelProperty(value = "任务状态：0待办 1已完成 2已取消", required = true)
    private Integer taskStatus;
}
