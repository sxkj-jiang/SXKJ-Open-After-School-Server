package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 请假申请AO - 用于家长/学生提交请假
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsLeaveApplyAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty(value = "课次ID")
    private Long sessionId;

    @ApiModelProperty(value = "请假类型：1-病假，2-事假，3-其他", required = true)
    @NotNull(message = "请假类型不能为空")
    private Integer leaveType;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "请假原因", required = true)
    @NotBlank(message = "请假原因不能为空")
    private String reason;

    @ApiModelProperty(value = "附件URL")
    private String attachmentUrl;
}
