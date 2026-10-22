package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据课程班ID查询课次详情集合 - 入参
 *
 * @author system
 * @date 2026-06-12
 */
@Data
@ApiModel("课程班课次详情查询参数")
public class AsCourseClassSessionDetailQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程班ID", required = true)
    @NotNull(message = "课程班ID不能为空")
    private Long courseClassId;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;
}
