package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生调课操作结果。
 */
@Data
@ApiModel("学生调课操作结果")
public class AsStudentAdjustOperateResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请ID")
    private Long requestId;

    @ApiModelProperty("申请状态")
    private Integer requestStatus;

    @ApiModelProperty("报名记录ID")
    private Long enrollmentId;

    @ApiModelProperty("提示信息")
    private String message;
}
