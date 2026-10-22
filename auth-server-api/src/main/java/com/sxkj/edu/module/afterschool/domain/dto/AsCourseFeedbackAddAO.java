package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 课程班反馈新增/更新AO
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@ApiModel("课程班反馈新增/更新对象")
public class AsCourseFeedbackAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "反馈ID(更新时必填)")
    private Long id;

    @NotNull(message = "所属学校不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "服务ID不能为空")
    @ApiModelProperty(value = "服务ID", required = true)
    private Long serviceId;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID", required = true)
    private Long courseClassId;

    @NotNull(message = "教师ID不能为空")
    @ApiModelProperty(value = "教师ID", required = true)
    private Long teacherId;

    @NotBlank(message = "反馈内容不能为空")
    @ApiModelProperty(value = "反馈内容", required = true)
    private String feedbackContent;

    @ApiModelProperty(value = "附件文件ID集合JSON")
    private String attachmentIds;

    @ApiModelProperty(value = "反馈时间")
    private Date feedbackTime;

    @ApiModelProperty(value = "反馈人用户ID")
    private Long creatorUserId;
}
