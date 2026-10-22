package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 巡课计划新增/编辑请求参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("巡课计划新增/编辑请求参数")
public class AsPatrolPlanAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("计划ID（编辑时必填）")
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @NotBlank(message = "计划名称不能为空")
    @ApiModelProperty(value = "计划名称", required = true)
    private String planName;

    @NotNull(message = "巡课类型不能为空")
    @ApiModelProperty(value = "巡课类型：1专项巡课 2日常巡课", required = true)
    private Integer patrolType;

    @NotBlank(message = "巡课人员教师ID不能为空")
    @ApiModelProperty("巡课人员教师ID集合（JSON数组字符串，前端传入原始JSON）")
    private String patrolUserIds;

    @NotBlank(message = "巡课范围年级ID不能为空")
    @ApiModelProperty("巡课范围年级ID集合（JSON数组字符串）")
    private String patrolScopeGradeJson;

    @NotBlank(message = "巡课范围课程类别大类ID不能为空")
    @ApiModelProperty("巡课范围课程类别大类ID集合（JSON数组字符串）")
    private String patrolScopeCourseTypeJson;

    @NotBlank(message = "巡课范围课程班ID不能为空")
    @ApiModelProperty("巡课范围课程班ID集合（JSON数组字符串）")
    private String patrolScopeCourseClassJson;

    @NotNull(message = "巡课开始时间不能为空")
    @ApiModelProperty(value = "巡课开始时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startTime;

    @NotNull(message = "巡课结束时间不能为空")
    @ApiModelProperty(value = "巡课结束时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("专项巡课检查项列表（专项巡课时必填）")
    private List<CheckItemVO> checkItems;

    @ApiModelProperty("巡课内容（JSON结构字符串）")
    private String patrolContent;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "计划状态：0草稿(待发布) 1已提交(发布) ", required = true)
    private Integer planStatus;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;
}
