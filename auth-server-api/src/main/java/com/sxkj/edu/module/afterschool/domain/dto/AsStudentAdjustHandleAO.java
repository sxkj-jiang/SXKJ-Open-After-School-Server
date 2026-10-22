package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生调课审核处理参数。
 */
@Data
@ApiModel("学生调课审核处理参数")
public class AsStudentAdjustHandleAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请ID", required = true)
    @NotNull(message = "申请ID不能为空")
    private Long requestId;

    @ApiModelProperty(value = "处理结果：1通过 2驳回", required = true)
    @NotNull(message = "处理结果不能为空")
    private Integer handleResult;

    @ApiModelProperty("审核意见；驳回时必填")
    private String handleOpinion;

    @ApiModelProperty("内部备注")
    private String remark;
}
