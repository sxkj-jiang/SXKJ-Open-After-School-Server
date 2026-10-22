package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 撤销请假申请入参对象
 *
 * @author jiangjj
 * @date 2026-05-31
 */
@Data
@ApiModel("撤销请假申请入参对象")
public class AsLeaveRequestCancelAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "家长ID", required = true)
    @NotNull(message = "家长ID不能为空")
    private Long guardianId;

    @ApiModelProperty(value = "请假记录ID", required = true)
    @NotNull(message = "请假记录ID不能为空")
    private Long leaveRequestId;
}
