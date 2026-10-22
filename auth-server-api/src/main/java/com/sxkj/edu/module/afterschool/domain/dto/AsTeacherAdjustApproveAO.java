package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 代课教师同意调课入参对象
 *
 * @author jiangjj
 * @date 2026-05-31
 */
@Data
@ApiModel("代课教师同意调课入参对象")
public class AsTeacherAdjustApproveAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课次ID", required = true)
    @NotNull(message = "课次ID不能为空")
    private Long sessionId;

    @ApiModelProperty(value = "调课ID", required = true)
    @NotNull(message = "调课ID不能为空")
    private Long adjustId;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

}
