package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课程班新增AO - 接口入参对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsCourseClassAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属学校ID", required = true)
    @NotNull(message = "所属学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "服务ID", required = true)
    @NotNull(message = "服务ID不能为空")
    private Long serviceId;

    @ApiModelProperty(value = "课程班名称", required = true)
    @NotBlank(message = "课程班名称不能为空")
    private String courseClassName;

    @ApiModelProperty(value = "课后类别ID", required = true)
    @NotNull(message = "课后类别ID不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "学习模块ID")
    private Long moduleId;

    @ApiModelProperty(value = "报名方式：1公开报名 2邀请报名 3行政班安排", required = true)
    @NotNull(message = "报名方式不能为空")
    private Integer enrollType;

    @ApiModelProperty(value = "行政班安排适用班级ID集合JSON")
    private String applicableClassIds;

    @ApiModelProperty(value = "邀请报名时是否需要家长确认：0否 1是")
    private Integer parentConfirmRequired;

    @ApiModelProperty(value = "公开报名性别要求：0不限 1男 2女")
    private Integer genderRequirement;

    @ApiModelProperty(value = "容量", required = true)
    @NotNull(message = "容量不能为空")
    private Integer capacity;

    @ApiModelProperty(value = "教师ID", required = true)
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    @ApiModelProperty(value = "地点名称")
    private String placeName;

    @ApiModelProperty(value = "课程介绍")
    private String intro;
}
