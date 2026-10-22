package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师调课操作结果。
 */
@Data
@ApiModel("教师调课操作结果")
public class AsTeacherAdjustOperateResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("调课申请ID")
    private Long requestId;

    @ApiModelProperty("申请单号")
    private String requestNo;

    @ApiModelProperty("申请状态")
    private Integer requestStatus;

    @ApiModelProperty("申请状态描述")
    private String requestStatusDesc;

    @ApiModelProperty("更新课次数")
    private Integer updatedSessionCount;
}
