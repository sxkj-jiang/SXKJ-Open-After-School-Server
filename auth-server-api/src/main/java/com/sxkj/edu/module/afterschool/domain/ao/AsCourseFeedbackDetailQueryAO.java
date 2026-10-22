package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课堂反馈详情查询AO（管理端）
 *
 * @author system
 * @date 2026-06-29
 */
@Data
@ApiModel("课堂反馈详情查询参数（管理端）")
public class AsCourseFeedbackDetailQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "反馈ID", required = true)
    @NotNull(message = "反馈ID不能为空")
    private Long id;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;
}
