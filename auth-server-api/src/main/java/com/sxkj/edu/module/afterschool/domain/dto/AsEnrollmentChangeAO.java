package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生选课改课对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课改课对象")
public class AsEnrollmentChangeAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "选课记录ID", required = true)
    @NotNull(message = "选课记录ID不能为空")
    private Long enrollmentId;

    @ApiModelProperty(value = "目标课程班ID", required = true)
    @NotNull(message = "目标课程班ID不能为空")
    private Long targetCourseClassId;

    @ApiModelProperty("备注")
    private String remark;
}
