package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 课后服务课次生成预览对象。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务课次生成预览对象")
public class AsServiceSessionPreviewAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课后服务ID", required = true)
    @NotNull(message = "课后服务ID不能为空")
    private Long serviceId;

    @ApiModelProperty("预览课程班；为空时使用服务下已保存课程和排课规则")
    @Valid
    private List<AsServiceCourseItemAO> courses;
}
