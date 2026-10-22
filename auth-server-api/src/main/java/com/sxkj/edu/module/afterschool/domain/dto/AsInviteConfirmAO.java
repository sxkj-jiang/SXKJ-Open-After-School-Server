package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 邀请报名确认对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("邀请报名确认对象")
public class AsInviteConfirmAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邀请码", required = true)
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty("是否确认")
    private Boolean confirm;
}
