package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课后类别新增/更新参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@ApiModel("课后类别新增/更新参数")
public class AsCourseCategoryAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类别ID(更新时必填)")
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @NotBlank(message = "类别名称不能为空")
    @ApiModelProperty(value = "类别名称", required = true)
    private String categoryName;

    @ApiModelProperty("父类别ID(更新时必填)")
    private Long parentId;

    @ApiModelProperty(value = "状态(1启用 0停用)",required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer enabledStatus;

    @NotNull(message = "排序号不能为空")
    @ApiModelProperty(value = "排序号", required = true)
    private Integer sortNo;

    @ApiModelProperty("备注")
    private String remark;
}
