package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 选课报名AO - 接口入参对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsEnrollmentAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属学校ID", required = true)
    @NotNull(message = "所属学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "服务ID", required = true)
    @NotNull(message = "服务ID不能为空")
    private Long serviceId;

    @ApiModelProperty(value = "课程班ID", required = true)
    @NotNull(message = "课程班ID不能为空")
    private Long courseClassId;

    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty(value = "报名来源：1家长公开报名 2教师邀请 3行政班安排", required = true)
    @NotNull(message = "报名来源不能为空")
    private Integer enrollSource;

    @ApiModelProperty(value = "来源邀请批次ID")
    private Long inviteId;
}
