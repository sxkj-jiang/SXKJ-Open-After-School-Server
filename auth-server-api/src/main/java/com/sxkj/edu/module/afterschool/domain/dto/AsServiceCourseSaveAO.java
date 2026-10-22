package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 课后服务第二步课程信息保存对象。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务课程信息保存对象")
public class AsServiceCourseSaveAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课后服务ID", required = true)
    @NotNull(message = "课后服务ID不能为空")
    private Long serviceId;

    @ApiModelProperty("是否同步重建课次")
    private Boolean rebuildSessions;

    @ApiModelProperty(value = "课程集合", required = true)
    @Valid
    @NotNull(message = "课程信息不能为空")
    private List<AsServiceCourseItemAO> courses;
}
