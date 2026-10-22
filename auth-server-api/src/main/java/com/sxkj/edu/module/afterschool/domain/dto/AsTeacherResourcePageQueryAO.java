package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师课程资源分页查询AO
 *
 * @author jiangjj
 * @date 2026-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师课程资源分页查询对象")
public class AsTeacherResourcePageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "课程类别ID不能为空")
    @ApiModelProperty(value = "课程类别ID（t_as_course_class.category_id）", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "资源名称（选填，模糊查询）")
    private String resourceName;

}
