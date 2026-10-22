package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理端作废教师调课申请参数。
 */
@Data
@ApiModel("管理端作废教师调课申请参数")
public class AsTeacherAdjustVoidAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("作废原因")
    private String reason;
}
