package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课次新增AO - 接口入参对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsCourseSessionAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程班ID", required = true)
    @NotNull(message = "课程班ID不能为空")
    private Long courseClassId;

    @ApiModelProperty(value = "课次序号", required = true)
    @NotNull(message = "课次序号不能为空")
    private Integer sessionIndex;

    @ApiModelProperty(value = "上课日期", required = true)
    @NotBlank(message = "上课日期不能为空")
    private String sessionDate;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "上课地点")
    private String placeName;

    @ApiModelProperty(value = "授课教师ID")
    private Long teacherId;

    @ApiModelProperty(value = "课程内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "课次开始日期时间")
    private String sessionStartDate;

    @ApiModelProperty(value = "课次结束日期时间")
    private String sessionEndDate;
}
