package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生去向预警-处理详情查看查询条件
 */
@Data
@ApiModel("学生去向预警-处理详情查看查询条件")
public class AsWarningHandleViewQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警记录ID", required = true)
    @NotNull(message = "预警记录ID不能为空")
    private Long id;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty(value = "课次ID", required = true)
    @NotNull(message = "课次ID不能为空")
    private Long sessionId;
}
