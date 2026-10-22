package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 取消选课对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("取消选课对象")
public class AsEnrollmentCancelAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("取消/驳回原因")
    private String cancelReason;
}
