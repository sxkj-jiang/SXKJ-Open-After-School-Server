package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 课后服务课程班保存项。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务课程班保存项")
public class AsServiceCourseItemAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID，新增时为空")
    private Long id;

    @ApiModelProperty(value = "课程班名称", required = true)
    @NotBlank(message = "课程班名称不能为空")
    private String courseClassName;

    @ApiModelProperty("课后类别ID")
    private Long categoryId;

    @ApiModelProperty("学习模块ID")
    private Long moduleId;

    @ApiModelProperty(value = "报名方式：1公开选课", required = true)
    @NotNull(message = "报名方式不能为空")
    private Integer enrollType;

    @ApiModelProperty("适用行政班ID集合JSON，格式为[1,2,3]")
    private String applicableClassIds;

    @ApiModelProperty("适用年级ID集合JSON，格式为[1,2,3]")
    private String applicableGradeIds;

    @ApiModelProperty("邀请报名时是否需要家长确认：0否 1是")
    private Integer parentConfirmRequired;

    @ApiModelProperty("公开报名性别要求：0不限 1男 2女")
    private Integer genderRequirement;

    @ApiModelProperty("课程班总容量；公开课必须为正整数")
    private Integer capacity;

    @ApiModelProperty("公开课必填：各适用年级的独立限额，合计不能超过capacity")
    @Valid
    private List<AsCourseGradeQuotaAO> gradeQuotas;

    @ApiModelProperty("授课教师ID")
    private Long teacherId;

    @ApiModelProperty("上课地点ID，多个用英文逗号分隔，如：1,2,3")
    private String placeId;

    @ApiModelProperty("课程介绍")
    private String intro;

    @ApiModelProperty("排课规则")
    @Valid
    private List<AsCourseScheduleRuleAO> scheduleRules;
}
